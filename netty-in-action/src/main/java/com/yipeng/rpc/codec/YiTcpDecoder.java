package com.yipeng.rpc.codec;

import com.google.common.base.Utf8;
import com.yipeng.rpc.model.YiData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class YiTcpDecoder extends ByteToMessageDecoder {
    public static final int MAGIC_LENGTH = 2;
    public static final int VERSION_LENGTH = 2;
    public static final int DATA_LENGTH_FIELD_LENGTH = 4;

    //es 2  version 2 length 4
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj = decodeObj(in);
        if (obj != null) {
            out.add(obj);
        }
    }


    private Object decodeObj(ByteBuf in) throws Exception{
        int readLength = in.readableBytes();
        if (readLength < MAGIC_LENGTH + VERSION_LENGTH + DATA_LENGTH_FIELD_LENGTH) {
            return null;
        }
        byte[] bytes = new byte[8];
        in.getBytes(0, bytes);
        String magic = new String(bytes, 0, 2);
        if (!"YI".equals(magic)) {  //非协议开头
            throw new Exception("no YI protocol");
        }
        short version = getShort(bytes, 2);
        int length = getInteger(bytes, 4);

        int totalLength = length + MAGIC_LENGTH + VERSION_LENGTH + DATA_LENGTH_FIELD_LENGTH;
        if (readLength < totalLength) {
            return null;
        }
        String data = in.toString(MAGIC_LENGTH + VERSION_LENGTH + DATA_LENGTH_FIELD_LENGTH, length, Charset.forName("utf8"));
        YiData data1 = new YiData();
        data1.setData(data);
        data1.setVersion(version);
        in.skipBytes(MAGIC_LENGTH + VERSION_LENGTH + DATA_LENGTH_FIELD_LENGTH + length);
        in.discardReadBytes();
        return data1;
    }

    public static short getShort(byte[] array, int i) {
        return (short) (array[i + 1] | (array[i] << 8));
    }

    public static int getInteger(byte[] array, int i) {
        return (int) (array[i + 3] | (array[i + 2] << 8) | (array[i + 1] << 16) | (array[i] << 24));
    }

}



