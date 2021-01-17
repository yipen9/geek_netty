package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PoolSubpageDemo {
    public static void main(String[] args) {
        ByteBuf[] byteBufs = new ByteBuf[100];
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer(2);
        /**{@link  io.netty.buffer.PoolSubpage#allocate}*/
        for (int i = 0; i < 100; i++) {
            byteBufs[i] = ByteBufAllocator.DEFAULT.heapBuffer(2);
        }
        ByteBuf lastone = ByteBufAllocator.DEFAULT.heapBuffer(2);

        lastone.release();

    }
}
