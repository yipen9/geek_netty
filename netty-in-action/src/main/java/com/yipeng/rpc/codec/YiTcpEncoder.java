package com.yipeng.rpc.codec;

import com.yipeng.rpc.model.YiData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class YiTcpEncoder extends MessageToByteEncoder<YiData> {

    @Override
    protected void encode(ChannelHandlerContext ctx, YiData msg, ByteBuf out) throws Exception {
        out.writeBytes("YI".getBytes());
        out.writeShort(msg.getVersion());
        byte[] datas = msg.getData().getBytes();
        out.writeInt(datas.length);
        out.writeBytes(datas);
    }
}
