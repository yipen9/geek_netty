package io.netty.demo.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.demo.rpc.model.TransportModel;
import io.netty.handler.codec.MessageToByteEncoder;


public class TransportEncoder extends MessageToByteEncoder<TransportModel> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TransportModel msg, ByteBuf out) throws Exception {
        msg.encode(out);
    }
}
