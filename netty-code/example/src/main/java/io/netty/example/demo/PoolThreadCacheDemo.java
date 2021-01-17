package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PoolThreadCacheDemo {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer(5);
        buf = null;
        buf = ByteBufAllocator.DEFAULT.heapBuffer(5);
    }
}
