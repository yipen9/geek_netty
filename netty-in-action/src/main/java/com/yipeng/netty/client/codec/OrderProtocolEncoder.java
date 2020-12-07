package com.yipeng.netty.client.codec;

import com.yipeng.netty.RequestMessage;
import com.yipeng.netty.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage requestMessage, List<Object> list) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        requestMessage.encode(buffer);
        list.add(buffer);
    }
}
