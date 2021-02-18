package io.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufAllocatorDemo {
    public static void main(String[] args) {
        ByteBuf[] array = new ByteBuf[1024];
        for (int i = 0; i < 1024; i++) {
            if (i == 1023) {
                array[i] = ByteBufAllocator.DEFAULT.heapBuffer(16 * 1024);
            }
            array[i] = ByteBufAllocator.DEFAULT.heapBuffer(16 * 1024);
        }

        ByteBuf buf1 = ByteBufAllocator.DEFAULT.heapBuffer(16 * 1024);
        buf1.writeInt(134343);
    }
}