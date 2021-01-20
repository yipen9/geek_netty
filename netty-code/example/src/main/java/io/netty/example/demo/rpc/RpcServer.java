package io.netty.example.demo.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.demo.rpc.codec.TransportDecoder;
import io.netty.example.demo.rpc.codec.TransportEncoder;
import io.netty.example.demo.rpc.hadler.RpcServerHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        serverBootstrap.group(boss, work).
                handler(new LoggingHandler(LogLevel.INFO)).
                channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TransportDecoder());
                        ch.pipeline().addLast(new TransportEncoder());
                        ch.pipeline().addLast(new RpcServerHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(8070);
        channelFuture.channel().closeFuture();

    }
}
