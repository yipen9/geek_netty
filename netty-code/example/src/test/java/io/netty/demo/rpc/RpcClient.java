package io.netty.demo.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.demo.rpc.codec.TransportDecoder;
import io.netty.demo.rpc.codec.TransportEncoder;
import io.netty.demo.rpc.hadler.RpcClientHandler;
import io.netty.demo.rpc.model.Request;
import io.netty.demo.rpc.model.Response;
import io.netty.demo.rpc.model.RpcFuture;
import io.netty.demo.rpc.model.TransportModel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new TransportDecoder());
                        ch.pipeline().addLast(new TransportEncoder());
                        ch.pipeline().addLast(new RpcClientHandler());
                    }
                });
    }

    public Response execute(Request request) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8070).sync();
//        channelFuture.channel().write(request);
        // Wait until the connection is closed.
        final Response response = new Response();
        response.setRequestId(request.getRequestId());
        RpcFuture futureTask = new RpcFuture(new Callable() {
            @Override
            public Response call() throws Exception {
                return response;
            }
        });
        futureTask.setResponse(response);
        TransportModel transportModel = new TransportModel();
        transportModel.setTransportId(request.getRequestId());
        transportModel.setTransportType((byte) 1);
        transportModel.setDataType(1);
        transportModel.setDatas(request.getMsg().getBytes());
        channelFuture.channel().writeAndFlush(transportModel);
        AttributeKey<RpcFuture> attributeKey = AttributeKey.valueOf("response");
        channelFuture.channel().attr(attributeKey).set(futureTask);
        Response response1 = null;
        try {
            response1 = (Response) futureTask.get();
            channelFuture.channel().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response1;
    }

    public static void main(String[] args) throws InterruptedException {
        try{
            RpcClient rpcClient = new RpcClient();
            Request request = new Request();
            request.setRequestId(123);
            request.setMsg("ni hao");
            Response response = rpcClient.execute(request);
            long start = System.currentTimeMillis();
            response = rpcClient.execute(request);
            long end = System.currentTimeMillis();
            System.out.println((end - start));
            System.out.println(response);
        }finally {
            bootstrap.group().shutdownGracefully();
        }
    }
}
