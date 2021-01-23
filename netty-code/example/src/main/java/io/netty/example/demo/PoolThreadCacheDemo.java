package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PoolThreadCacheDemo {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer(5);
        buf.release();
        for (int i = 0; i < 8190; i++) {    /** 大于{@link io.netty.buffer.PoolThreadCache#freeSweepAllocationThreshold}*/
            buf = ByteBufAllocator.DEFAULT.heapBuffer(16 % (i+1));
            buf.release();
        }
        buf = ByteBufAllocator.DEFAULT.heapBuffer(5);
        buf.release();
    }
}
