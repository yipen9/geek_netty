package io.netty.example.demo.rpc.hadler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.example.demo.rpc.model.Response;
import io.netty.example.demo.rpc.model.RpcFuture;
import io.netty.example.demo.rpc.model.TransportModel;
import io.netty.util.AttributeKey;

public class RpcClientHandler extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransportModel) {
            Channel channel = ctx.channel();
            AttributeKey<RpcFuture> attributeKey = AttributeKey.valueOf("response");
            RpcFuture rpcFuture = channel.attr(attributeKey).get();
            Response response = rpcFuture.getResponse();
            if (response.getRequestId() == ((TransportModel) msg).getTransportId()) {
                response.setMessage(new String(((TransportModel) msg).getDatas()));
            }
            rpcFuture.run();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg);
    }
}
