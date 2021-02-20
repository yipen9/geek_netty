package io.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

public class PoolSubpageDemo {
    @Test
    public void testPoolSubPgae() {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(511);

        byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(511);

        System.out.println(byteBuf);
    }
}
