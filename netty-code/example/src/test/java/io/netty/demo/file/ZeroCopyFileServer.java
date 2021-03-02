package io.netty.demo.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.demo.file.FileServerHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;

public class ZeroCopyFileServer {
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        final ByteBuf delimiter = Unpooled.copiedBuffer(";".getBytes("UTF-8"));
        serverBootstrap.group(boss, work)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)//可连接队列
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();     //decode从上到下
                        cp.addLast(new LoggingHandler(LogLevel.INFO));
                        cp.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        cp.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        cp.addLast(new FileCopyHandler());
                        cp.addLast(new StringAndFileEncoder(CharsetUtil.UTF_8));
                    }
                });

        ChannelFuture future = serverBootstrap.bind(7777).sync();
        future.channel().closeFuture();
    }
}
