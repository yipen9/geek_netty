package com.yipeng.file.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class FileNioServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();

        NioEventLoopGroup worker = new NioEventLoopGroup();

        ChannelFuture channelFuture =
                serverBootstrap.channel(ServerSocketChannel.class)
                .group(boss, worker).handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) throws Exception {
                        ch.pipeline().addLast();

                    }
                }).bind(7070);

    }
}
