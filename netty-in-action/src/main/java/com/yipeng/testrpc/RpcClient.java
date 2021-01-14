package com.yipeng.testrpc;

import com.yipeng.rpc.codec.YiTcpDecoder;
import com.yipeng.rpc.codec.YiTcpEncoder;
import com.yipeng.rpc.model.YiData;
import com.yipeng.testrpc.codec.RPCFrameDeCoder;
import com.yipeng.testrpc.codec.RPCFrameEncoder;
import com.yipeng.testrpc.codec.RPCProtocolDecoder;
import com.yipeng.testrpc.codec.RPCProtocolEncoder;
import com.yipeng.testrpc.handler.RpcClientHandler;
import com.yipeng.testrpc.model.LogMessage;
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
//                pipeline.addLast(new RPCFrameDeCoder());
//                pipeline.addLast(new RPCProtocolDecoder());
//
//                pipeline.addLast(new RPCFrameEncoder());
//                pipeline.addLast(new RPCProtocolEncoder());

                pipeline.addLast(new YiTcpDecoder());
                pipeline.addLast(new YiTcpEncoder());
                pipeline.addLast(rpcClientHandler);
                pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8027).sync();
        YiData yiData = new YiData();
        yiData.setVersion((short) 123);

//        rpcClientHandler.register(logMessage);
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                yiData.setData("hello world!");
                ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(yiData);
            }else{
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < 1024; j++) {
                    sb.append("a");
                }
                yiData.setData(sb.toString());
                ChannelFuture channelFuture1 = channelFuture.channel().writeAndFlush(yiData);
            }
        }

        channelFuture.channel().closeFuture().sync();
    }
}
