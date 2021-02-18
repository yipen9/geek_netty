package io.netty.demo.rpc.hadler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.demo.rpc.model.TransportModel;

public class RpcServerHandler extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransportModel) {
            String data = new String(((TransportModel) msg).getDatas());
            data = "server " + data;
            ((TransportModel) msg).setDatas(data.getBytes());
            ctx.writeAndFlush(msg);
        }
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

    }
}
