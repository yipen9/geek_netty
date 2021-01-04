package com.yipeng.netty.handle;

import com.yipeng.netty.RequestMessage;
import com.yipeng.netty.common.Operation;
import com.yipeng.netty.common.OperationResult;
import com.yipeng.netty.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class OrderServerHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();

        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);
        channelHandlerContext.channel();
        channelHandlerContext.writeAndFlush(responseMessage);
    }
}
