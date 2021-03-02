package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;

import static io.netty.demo.file.RevHandler.FILE_LENGTH_KEY;
import static io.netty.demo.file.RevHandler.REV_FILE_KEY;

public class WriteFileHandler extends ChannelInboundHandlerAdapter {
    int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (ctx.channel().hasAttr(REV_FILE_KEY)) {
            RandomAccessFile randomAccessFile = ctx.channel().attr(REV_FILE_KEY).get();
            Integer length = ctx.channel().attr(FILE_LENGTH_KEY).get();
            if (obj instanceof ByteBuf) {
                ByteBuf msg = (ByteBuf) obj;
                if (count + msg.readableBytes() > length) {
                    if (count < length) {
                        ByteBuf byteBuf = msg.readSlice(length - count);
                        randomAccessFile.getChannel().write(byteBuf.nioBuffer());
                        byteBuf.release();
                    }
                    if (msg.readableBytes() == 4) {
                        String msgStr = msg.toString(CharsetUtil.UTF_8);
                        if (msgStr.equals("end;")) {
                            ctx.fireChannelRead(msg);
                        }
                    }
                }else{
                    count += msg.readableBytes();
                    randomAccessFile.getChannel().write(msg.nioBuffer());
                    msg.release();
                }
            }
        }else{
            ctx.fireChannelRead(obj);
        }
    }
}
