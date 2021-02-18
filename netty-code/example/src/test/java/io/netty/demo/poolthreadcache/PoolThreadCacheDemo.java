package io.netty.demo.poolthreadcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

import java.nio.ByteBuffer;

public class PoolThreadCacheDemo {
    @Test
    public void testTinyCache() {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(4);

        byteBuf.setBytes(0, "1234".getBytes());

        ByteBuffer buffer = byteBuf.nioBuffer(0, 4);

        ByteBuffer iBuffer = byteBuf.internalNioBuffer(0, 4);

        //会调用io.netty.buffer.PoolArena#free
        byteBuf.release();

        //free方法会将生成的加入到cache中io.netty.buffer.PoolThreadCache.add
        byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(4);

        System.out.println(byteBuf);
    }
}
