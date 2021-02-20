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


    /**
     * 0100000000000000000000000000000000000000000000000000000000000000
     * 0000000000000000000000100000000000000000000000000000000000000000
     */
    @Test
    public void testToHandle() {
        long c = 0x4000000000000000L;
        System.out.println(getBinStr(c));
        long d = 8 << 6;
        long f = d << 32;
        System.out.println(getBinStr(f));

        long e = c | f;
        int x = (int)(e >> 32); //获得高32位
        System.out.println(getBinStr(x));
        int z = 0x3FFFFFFF;
        System.out.println(getBinStr(z)); //00111111111111111111111111111111
        long k = x & z;         //bitmapIdx
        System.out.println(k); //512
    }


    public String getBinStr(long c) {
        String bitStr = Long.toBinaryString(c);
        int len = 64 - bitStr.length();
        for (int i = 0; i < len; i++) {
            bitStr = "0" + bitStr;
        }
        return bitStr;
    }

    public String getBinStr(int c) {
        String bitStr = Long.toBinaryString(c);
        int len = 32 - bitStr.length();
        for (int i = 0; i < len; i++) {
            bitStr = "0" + bitStr;
        }
        return bitStr;
    }
}
