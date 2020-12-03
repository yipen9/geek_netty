package com.yipeng.netty.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    /**
     * maxFrameLength  发送的数据包最大长度
     * lengthFieldOffset  长度域偏移量，指的是长度域位于整个数据包字节数组中的下标
     * lengthFieldLength - 长度域的自己的字节数长度。
     * lengthAdjustment – 长度域的偏移量矫正。 如果长度域的值，除了包含有效数据域的长度外，
     * 还包含了其他域（如长度域自身）长度，那么，就需要进行矫正。(lengthFieldLength如果不是表示正在有效数据的长度，需要矫正)
     * 矫正的值为：包长 - 长度域的值 – 长度域偏移 – 长度域长。
     * initialBytesToStrip – 丢弃的起始字节数。丢弃处于有效数据前面的字节数量。
     * 比如前面有4个节点的长度域，则它的值为4。
     */

    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }

    /**
     *   new LengthFieldBasedFrameDecoder(1024,2,4,-4,6);
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuf buf = Unpooled.buffer();
        String s = "peng.yi" + " is me ,呵呵";
        byte[] bytes = s.getBytes("UTF-8");
        buf.writeChar(100); //占用2个字节
        buf.writeInt(bytes.length + 4); //占用4个字节
        buf.writeBytes(bytes);
        System.out.println(Arrays.toString(buf.array()));
    }
}
