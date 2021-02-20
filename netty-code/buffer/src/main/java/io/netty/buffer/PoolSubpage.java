/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.netty.buffer;

/**
 * 可参考：https://www.jianshu.com/p/d91060311437
 * @param <T>
 */
final class PoolSubpage<T> implements PoolSubpageMetric {

    final PoolChunk<T> chunk;
    private final int memoryMapIdx;//内存映射索引
    private final int runOffset;//chunk中的偏移
    private final int pageSize;//页大小
    private final long[] bitmap;//64位的位图数组，描述某一尺寸内存的状态 每一位都可以表示是该内存否可用了 1表示不可用 0表示可用

    PoolSubpage<T> prev;//前驱
    PoolSubpage<T> next;//后继

    boolean doNotDestroy;//是否销毁
    int elemSize;//分割的内存尺寸大小
    private int maxNumElems;//一页存能存多少个尺寸都为elemSize的内存，即最大可分配的内存数
    private int bitmapLength;//实际用到的位图数
    private int nextAvail;//下一个可用的位图索引
    private int numAvail;//可用内存的个数

    // TODO: Test if adding padding helps under contention
    //private long pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;

    /** Special constructor that creates a linked list head */
    PoolSubpage(int pageSize) {
        chunk = null;
        memoryMapIdx = -1;
        runOffset = -1;
        elemSize = -1;
        this.pageSize = pageSize;
        bitmap = null;
    }

    PoolSubpage(PoolSubpage<T> head, PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
        this.chunk = chunk;//所在块
        this.memoryMapIdx = memoryMapIdx;//内存映射索引
        this.runOffset = runOffset;//块内偏移
        this.pageSize = pageSize;//页大小
        //最多需要多少个long类型的位图来描述一页分割成16B(Tiny最小单位)的所有内存的状态,long是64位
        bitmap = new long[pageSize >>> 10]; // pageSize / 16 / 64
        init(head, elemSize);
    }

    void init(PoolSubpage<T> head, int elemSize) {
        doNotDestroy = true;
        this.elemSize = elemSize;
        if (elemSize != 0) {
            maxNumElems = numAvail = pageSize / elemSize;//获取能分割成多少elemSize大小的内存
            nextAvail = 0;
            bitmapLength = maxNumElems >>> 6;//实际需要用的到的long类型的位图的个数，maxNumElems/64,一个long长度64位
            if ((maxNumElems & 63) != 0) {//有余数就多一个
                bitmapLength ++;
            }
            //实际用到的位图数
            for (int i = 0; i < bitmapLength; i ++) {
                bitmap[i] = 0;
            }
        }
        //将当前子页加到head的后面，是个双向循环链表。
        addToPool(head);
    }

    /**
     * Returns the bitmap index of the subpage allocation.
     */
    long allocate() {
        if (elemSize == 0) {
            return toHandle(0);
        }

        if (numAvail == 0 || !doNotDestroy) {//没有可用的容量或者要销毁
            return -1;
        }
        //获取下一个可用的位图索引，不是位图数组
        final int bitmapIdx = getNextAvail();
        int q = bitmapIdx >>> 6;    //右移获取高位，获取bitmap的下标
        int r = bitmapIdx & 63;     //获取低位，也就是bits中的位置
        assert (bitmap[q] >>> r & 1) == 0;
        bitmap[q] |= 1L << r;       //将r位上的标识为1，表示已经使用

        if (-- numAvail == 0) {
            removeFromPool();
        }

        return toHandle(bitmapIdx);
    }

    /**
     * @return {@code true} if this subpage is in use.
     *         {@code false} if this subpage is not used by its chunk and thus it's OK to be released.
     */
    boolean free(PoolSubpage<T> head, int bitmapIdx) {
        if (elemSize == 0) {
            return true;
        }
        int q = bitmapIdx >>> 6;
        int r = bitmapIdx & 63;
        assert (bitmap[q] >>> r & 1) != 0;
        bitmap[q] ^= 1L << r;

        setNextAvail(bitmapIdx);

        if (numAvail ++ == 0) {
            addToPool(head);
            return true;
        }

        if (numAvail != maxNumElems) {
            return true;
        } else {
            // Subpage not in use (numAvail == maxNumElems)
            if (prev == next) {
                // Do not remove if this subpage is the only one left in the pool.
                return true;
            }

            // Remove this subpage from the pool if there are other subpages left in the pool.
            doNotDestroy = false;
            removeFromPool();
            return false;
        }
    }

    private void addToPool(PoolSubpage<T> head) {
        assert prev == null && next == null;
        prev = head;
        next = head.next;
        next.prev = this;
        head.next = this;
    }

    private void removeFromPool() {
        assert prev != null && next != null;
        prev.next = next;
        next.prev = prev;
        next = null;
        prev = null;
    }

    private void setNextAvail(int bitmapIdx) {
        nextAvail = bitmapIdx;
    }

    private int getNextAvail() {
        int nextAvail = this.nextAvail;
        if (nextAvail >= 0) {
            this.nextAvail = -1;
            return nextAvail;//第一次直接返回，后面就要findNextAvail
        }
        return findNextAvail();//-1表示要找
    }

    private int findNextAvail() {
        final long[] bitmap = this.bitmap;
        final int bitmapLength = this.bitmapLength;
        for (int i = 0; i < bitmapLength; i ++) {
            long bits = bitmap[i];
            if (~bits != 0) {   //如果全部分配完，bits为0xFFFFFFFFFFFFFFFF说明这个long所描述的64个内存段还有未分配的；
                return findNextAvail0(i, bits);
            }
        }
        return -1;
    }

    private int findNextAvail0(int i, long bits) {
        final int maxNumElems = this.maxNumElems;//最大可分配数
        final int baseVal = i << 6;//左移6位，前面高位，存bitmap数组的下标，也就是i。后面bits可用的位置
        //遍历位图的每一位，从最低位开始遍历，0表示没有过
        for (int j = 0; j < 64; j ++) {//j表示位图里第j位，从低位到高位0-63
            if ((bits & 1) == 0) {//取出最低位，为0表示可用
                //加上位置序号j后的新的索引值，比如开始是0，第一个+0索引就是0，然后+1，索引1，类似最后索引是baseVal+63
                int val = baseVal | j;
                ////如果索引没到最大可分配数就返回，其实最大索引就是maxNumElems-1
                if (val < maxNumElems) {
                    return val;
                } else {
                    break;//等于就不行了，跳出循环
                }
            }
            bits >>>= 1;//位图右移，即从低位往高位，进行比较
        }
        return -1;
    }

    /**
     * 如果没有加上0x4000000000000000L，第一次分配bitmapIdx=0，返回的也是2048。这样在后面的initBuf方法就会出问题
     * 0x4000000000000000L为高位标示符+bitmapIdx左移32位，不会和高位重叠；+memoryMapIdx低位32位
     * @param bitmapIdx
     * @return
     */
    private long toHandle(int bitmapIdx) {
        return 0x4000000000000000L | (long) bitmapIdx << 32 | memoryMapIdx;
    }

    public static void main(String[] args) {
        System.out.println( 1 << 32 | 1028);

    }

    @Override
    public String toString() {
        final boolean doNotDestroy;
        final int maxNumElems;
        final int numAvail;
        final int elemSize;
        if (chunk == null) {
            // This is the head so there is no need to synchronize at all as these never change.
            doNotDestroy = true;
            maxNumElems = 0;
            numAvail = 0;
            elemSize = -1;
        } else {
            synchronized (chunk.arena) {
                if (!this.doNotDestroy) {
                    doNotDestroy = false;
                    // Not used for creating the String.
                    maxNumElems = numAvail = elemSize = -1;
                } else {
                    doNotDestroy = true;
                    maxNumElems = this.maxNumElems;
                    numAvail = this.numAvail;
                    elemSize = this.elemSize;
                }
            }
        }

        if (!doNotDestroy) {
            return "(" + memoryMapIdx + ": not in use)";
        }

        return "(" + memoryMapIdx + ": " + (maxNumElems - numAvail) + '/' + maxNumElems +
                ", offset: " + runOffset + ", length: " + pageSize + ", elemSize: " + elemSize + ')';
    }

    @Override
    public int maxNumElements() {
        if (chunk == null) {
            // It's the head.
            return 0;
        }

        synchronized (chunk.arena) {
            return maxNumElems;
        }
    }

    @Override
    public int numAvailable() {
        if (chunk == null) {
            // It's the head.
            return 0;
        }

        synchronized (chunk.arena) {
            return numAvail;
        }
    }

    @Override
    public int elementSize() {
        if (chunk == null) {
            // It's the head.
            return -1;
        }

        synchronized (chunk.arena) {
            return elemSize;
        }
    }

    @Override
    public int pageSize() {
        return pageSize;
    }

    void destroy() {
        if (chunk != null) {
            chunk.destroy();
        }
    }
}
