package io.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

public class PoolArenaDemo {
    /**
     * 测试的normalizedCapacity，小于一个chunkSize，也就是1024*1024*16
     * 1024*1024*16
     */

    public void normalizedCapacity(int value) {
        int normalizedCapacity = value;
        normalizedCapacity--;
        normalizedCapacity |= normalizedCapacity >>> 1;
        normalizedCapacity |= normalizedCapacity >>> 2;
        normalizedCapacity |= normalizedCapacity >>> 4;
        normalizedCapacity |= normalizedCapacity >>> 8;
        normalizedCapacity |= normalizedCapacity >>> 16;
        normalizedCapacity++;
        if (normalizedCapacity < 0) {
            normalizedCapacity >>>= 1;
        }
        System.out.println(getLog2(value) + " : " + getLog2(normalizedCapacity) + " -> " + normalizedCapacity);
    }

    //tiny  :
    //small :

    @Test
    public void testNormalizedCapacity() {
        int a = (int) getLog2(16 * 1024 * 1024);
        for (int i = 1; i <= a; i++) {
            normalizedCapacity((1 << i) - 1);
        }
    }


    @Test
    public void testAllocateChunkSize() {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(1024 * 1024 * 16 - 1);
        System.out.println(byteBuf);
    }

    public static double getLog2(int value) {
        return Math.log(value) / Math.log(2);
    }

    static boolean isTiny(int normCapacity) {
        return (normCapacity & 0xFFFFFE00) == 0;
    }

    @Test
    public void testSmall() {
        boolean isTiny = isTiny(1024 * 1024 * 5 + 1023);
        System.out.println(isTiny);
    }

}
