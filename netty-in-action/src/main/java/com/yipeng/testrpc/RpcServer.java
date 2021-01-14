package com.yipeng.testrpc;

import com.yipeng.rpc.codec.YiTcpDecoder;
import com.yipeng.rpc.codec.YiTcpEncoder;
import com.yipeng.testrpc.codec.RPCFrameDeCoder;
import com.yipeng.testrpc.codec.RPCFrameEncoder;
import com.yipeng.testrpc.codec.RPCProtocolDecoder;
import com.yipeng.testrpc.codec.RPCProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        final Dispatcher dispatcher = new Dispatcher();
        serverBootstrap.group(boss, worker).
                channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new RPCFrameDeCoder());
//                        ch.pipeline().addLast(new RPCProtocolDecoder());
                        ch.pipeline().addLast(new YiTcpDecoder());
//                        ch.pipeline().addLast(new RPCFrameEncoder());
//                        ch.pipeline().addLast(new RPCProtocolEncoder());
                        ch.pipeline().addLast(new YiTcpEncoder());

                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));

                        ch.pipeline().addLast(dispatcher);
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(8027).sync();

        channelFuture.channel().closeFuture().sync();
    }
}
