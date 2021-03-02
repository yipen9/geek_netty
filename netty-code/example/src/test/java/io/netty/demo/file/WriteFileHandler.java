package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;

import static io.netty.demo.file.RevHandler.REV_FILE_KEY;

public class WriteFileHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        boolean done = false;
        if (msg.readableBytes() == 3) {
            String msgStr = msg.toString(CharsetUtil.UTF_8);
            if (msgStr.equals("end")) {
                done = true;
                ctx.fireChannelRead(msg);
            }
        }
        if (!done) {
            if (ctx.channel().hasAttr(REV_FILE_KEY)) {
                RandomAccessFile randomAccessFile = ctx.channel().attr(REV_FILE_KEY).get();
                randomAccessFile.getChannel().write(msg.nioBuffer());
            }else{
                ctx.fireChannelRead(msg);
            }
        }
    }
}
