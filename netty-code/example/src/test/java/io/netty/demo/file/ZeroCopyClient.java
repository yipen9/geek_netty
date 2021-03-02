package io.netty.demo.file;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ZeroCopyClient {
    public static void main(String[] args) throws IOException, InterruptedException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//        String line = in.readLine();
        final ByteBuf delimiter = Unpooled.wrappedBuffer(";".getBytes());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline cp = ch.pipeline();
                        cp.addLast(new WriteFileHandler());
                        cp.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        cp.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        cp.addLast(new RevHandler());
                        cp.addLast(new LoggingHandler(LogLevel.INFO));
                        cp.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7777).sync();
        channelFuture.channel().writeAndFlush("cp D:\\elasticsearch\\elasticsearch-7.9.0-windows-x86_64.zip D:\\;");
        channelFuture.channel().closeFuture().sync();
    }
}
