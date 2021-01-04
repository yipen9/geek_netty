package com.yipeng.netty.server;

import com.yipeng.netty.handle.OrderServerHandler;
import com.yipeng.netty.server.codec.OrderFrameDecoder;
import com.yipeng.netty.server.codec.OrderFrameEncoder;
import com.yipeng.netty.server.codec.OrderProtocolDecoder;
import com.yipeng.netty.server.codec.OrderProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class OrderServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            serverBootstrap.group(group);

            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());

                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                    pipeline.addLast(new OrderServerHandler());
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();


            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }
}
