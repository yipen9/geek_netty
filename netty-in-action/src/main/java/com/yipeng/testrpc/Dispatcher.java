package com.yipeng.testrpc;

import com.yipeng.testrpc.handler.Handler;
import com.yipeng.testrpc.handler.LogHandler;
import com.yipeng.testrpc.model.LogMessage;
import com.yipeng.testrpc.model.RPCMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class Dispatcher extends SimpleChannelInboundHandler<RPCMessage> {
    private static ConcurrentHashMap<String, Handler> handleMap = new ConcurrentHashMap<>();

    static {
        handleMap.put(LogMessage.type, new LogHandler());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCMessage msg) throws Exception {
        if (msg.getRequestType().equals(LogMessage.type)) {
            LogMessage logMessage = new LogMessage();
            logMessage.setRequestId(msg.getRequestId());
            logMessage.setMessageBody("from server " + msg.getMessageBody());
            ctx.writeAndFlush(logMessage);
        }
    }
}
