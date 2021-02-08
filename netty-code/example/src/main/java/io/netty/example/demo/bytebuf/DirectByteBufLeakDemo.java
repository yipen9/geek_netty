package io.netty.example.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

public class DirectByteBufLeakDemo {
    /**
     * 模拟内存泄漏
     * -Xmx10m -XX:MaxDirectMemorySize=4m -Dio.netty.leakDetectionLevel=paranoid
     * @param args
     */

    /***
     * 17:09:58.898 [main] ERROR io.netty.util.ResourceLeakDetector - LEAK: ByteBuf.release() was not called before it's garbage-collected. See https://netty.io/wiki/reference-counted-objects.html for more information.
     * Recent access records:
     * Created at:
     * 	io.netty.buffer.PooledByteBufAllocator.newDirectBuffer(PooledByteBufAllocator.java:357)
     * 	io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:197)
     * 	io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:188)
     * 	io.netty.example.demo.bytebuf.DirectByteBufLeakDemo.main(DirectByteBufLeakDemo.java:14)
     *
     * 	泄漏检查，是在new 相关的时候执行检查的，
     * 	{@link io.netty.buffer.AbstractByteBufAllocator#toLeakAwareBuffer(io.netty.buffer.ByteBuf)}
     */
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(7);
            buf.writeBytes("1234123".getBytes());
        }
        System.out.println(123);
    }
}
