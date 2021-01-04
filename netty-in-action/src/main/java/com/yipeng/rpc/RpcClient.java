package com.yipeng.rpc;

import com.yipeng.netty.RequestMessage;
import com.yipeng.netty.client.codec.OrderFrameDecoder;
import com.yipeng.netty.client.codec.OrderFrameEncoder;
import com.yipeng.netty.client.codec.OrderProtocolDecoder;
import com.yipeng.netty.client.codec.OrderProtocolEncoder;
import com.yipeng.netty.common.order.OrderOperation;
import com.yipeng.rpc.codec.RPCFrameDeCoder;
import com.yipeng.rpc.codec.RPCFrameEncoder;
import com.yipeng.rpc.codec.RPCProtocolDecoder;
import com.yipeng.rpc.codec.RPCProtocolEncoder;
import com.yipeng.rpc.handler.RpcClientHandler;
import com.yipeng.rpc.model.LogMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class);
        final RpcClientHandler rpcClientHandler = new RpcClientHandler();
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new RPCFrameDeCoder());
                pipeline.addLast(new RPCProtocolDecoder());

                pipeline.addLast(new RPCFrameEncoder());
                pipeline.addLast(new RPCProtocolEncoder());

                pipeline.addLast(rpcClientHandler);
                pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8027).sync();
        LogMessage logMessage = new LogMessage();
        logMessage.setRequestId(1111l);
        logMessage.setMessageBody("aaaaaaaaaaaaaaaaa");
        rpcClientHandler.register(logMessage);
        channelFuture.channel().writeAndFlush(logMessage);
        channelFuture.channel().closeFuture().sync();
    }
}
