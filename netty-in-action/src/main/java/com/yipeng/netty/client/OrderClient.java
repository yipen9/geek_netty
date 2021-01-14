package com.yipeng.netty.client;

import com.yipeng.netty.RequestMessage;
import com.yipeng.netty.client.codec.OrderFrameEncoder;
import com.yipeng.netty.client.codec.OrderProtocolDecoder;
import com.yipeng.netty.client.codec.OrderFrameDecoder;
import com.yipeng.netty.client.codec.OrderProtocolEncoder;
import com.yipeng.netty.common.order.OrderOperation;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;



public class OrderClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            bootstrap.group(group);

            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(new OrderFrameEncoder());
                    pipeline.addLast(new OrderProtocolEncoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
            RequestMessage requestMessage = new RequestMessage(1234l, new OrderOperation(123, "dish 1234"));
            channelFuture.channel().writeAndFlush(requestMessage);
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
