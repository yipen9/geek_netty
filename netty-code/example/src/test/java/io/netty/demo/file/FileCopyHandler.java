package io.netty.demo.file;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.io.RandomAccessFile;

public class FileCopyHandler extends SimpleChannelInboundHandler<String> {
    public static AttributeKey<Boolean> FILE_IS_WRITING_KEY = AttributeKey.valueOf("FILE_IS_WRITING_KEY");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String[] array = msg.split(" ");
        if (array.length != 3) {
            ctx.writeAndFlush("you input ERROR,please cp sourcePath targetPath");
            return;
        }
        String command = array[0];
        String source = array[1];
        String target = array[2];

        if (!command.equals("cp")) {
            ctx.writeAndFlush("start cp");
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(source, "rw");
        DefaultFileRegion defaultFileRegion = new DefaultFileRegion(randomAccessFile.getChannel(), 0l, randomAccessFile.length());

        ctx.channel().writeAndFlush("rev " + target + "abc.rar" + ";");

        ctx.channel().attr(FILE_IS_WRITING_KEY).set(true);
        ChannelFuture channelFuture = ctx.writeAndFlush(defaultFileRegion, ctx.newProgressivePromise());
        channelFuture.addListener(new ChannelProgressiveFutureListener(){
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("done");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                System.out.println("process : " + progress * 1.0 / total);
            }
        });
        if (channelFuture.isDone()) {
            ctx.channel().attr(FILE_IS_WRITING_KEY).set(null);
        }
        if (channelFuture.isSuccess()) {
            ctx.writeAndFlush("end");
        }
    }
}
