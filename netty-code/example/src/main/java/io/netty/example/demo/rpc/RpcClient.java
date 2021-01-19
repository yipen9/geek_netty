package io.netty.example.demo.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.demo.rpc.codec.TransportDecoder;
import io.netty.example.demo.rpc.codec.TransportEncoder;
import io.netty.example.demo.rpc.hadler.RpcClientHandler;
import io.netty.example.demo.rpc.model.Request;
import io.netty.example.demo.rpc.model.Response;
import io.netty.example.demo.rpc.model.RpcFuture;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class RpcClient {
    static Bootstrap bootstrap = new Bootstrap();
    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).
                channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TransportDecoder());
                        ch.pipeline().addLast(new TransportEncoder());
                        ch.pipeline().addLast(new RpcClientHandler());
                    }
                });
    }

    public Response execute(Request request) {
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8070);
        channelFuture.channel().write(request);
        // Wait until the connection is closed.
        final Response response = new Response();
        response.setRequestId(request.getRequestId());
        RpcFuture futureTask = new RpcFuture(new Callable() {
            @Override
            public Response call() throws Exception {
                return response;
            }
        });
        AttributeKey<Response> attributeKey = AttributeKey.valueOf("response", response);
        channelFuture.channel().attr(response);

        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
