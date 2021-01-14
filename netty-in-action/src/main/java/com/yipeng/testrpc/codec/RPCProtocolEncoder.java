package com.yipeng.testrpc.codec;

import com.yipeng.testrpc.model.RPCMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class RPCProtocolEncoder extends MessageToMessageEncoder<RPCMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RPCMessage msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        msg.encode(byteBuf);
        out.add(byteBuf);
    }
}
