package io.netty.demo.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.File;
import java.io.RandomAccessFile;

public class RevHandler extends SimpleChannelInboundHandler<String> {
    public static AttributeKey<RandomAccessFile> REV_FILE_KEY = AttributeKey.valueOf("REV_FILE_KEY");
    public static AttributeKey<Integer> FILE_LENGTH_KEY = AttributeKey.valueOf("FILE_LENGTH_KEY");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (msg.equals("end") && ctx.channel().hasAttr(REV_FILE_KEY)) {
            RandomAccessFile randomAccessFile = ctx.channel().attr(REV_FILE_KEY).get();
            randomAccessFile.close();
            ctx.channel().attr(REV_FILE_KEY).set(null);
            ctx.channel().attr(FILE_LENGTH_KEY).set(null);
        }
        String[] array = msg.split(" ");
        if (array.length != 3) {
            ctx.writeAndFlush("you return ERROR,please rev targetPath");
            return;
        }
        String command = array[0];
        String target = array[1];
        String length = array[2];
        if (!command.equals("rev")) {
            ctx.writeAndFlush("start rev");
        }
        File f = new File(target);
        if (!f.exists()) {
            f.createNewFile();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(target, "rw");
        ctx.channel().attr(REV_FILE_KEY).set(randomAccessFile);
        ctx.channel().attr(FILE_LENGTH_KEY).set(Integer.parseInt(length));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().hasAttr(REV_FILE_KEY)) {
            ctx.channel().attr(REV_FILE_KEY).set(null);
        }
    }
}
