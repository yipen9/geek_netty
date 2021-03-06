package com.yipeng.netty.server.codec;

import com.yipeng.netty.RequestMessage;
import com.yipeng.netty.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> list) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        responseMessage.encode(buffer);
        list.add(buffer);
    }
}
