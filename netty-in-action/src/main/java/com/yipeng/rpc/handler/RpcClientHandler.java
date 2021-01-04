package com.yipeng.rpc.handler;

import com.yipeng.rpc.model.RPCMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<RPCMessage> {
    private static ConcurrentHashMap<Long, RPCMessage> handleMap = new ConcurrentHashMap<>();

    public boolean register(RPCMessage rpcMessage) {
        handleMap.put(rpcMessage.getRequestId(), rpcMessage);
        return true;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCMessage msg) throws Exception {
        if (handleMap.get(msg.getRequestId()) != null) {
            System.out.println(msg);
            handleMap.remove(msg.getRequestId());
        }
    }
}
