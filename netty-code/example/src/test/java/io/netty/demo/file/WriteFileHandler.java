package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;

import static io.netty.demo.file.RevHandler.*;

public class WriteFileHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (ctx.channel().hasAttr(REV_FILE_KEY)) {  //判断是否正在进行写数据。
            RandomAccessFile randomAccessFile = ctx.channel().attr(REV_FILE_KEY).get();
            Integer length = ctx.channel().attr(FILE_LENGTH_KEY).get();
            if (obj instanceof ByteBuf) {
                Integer count = ctx.channel().attr(CURRENT_FILE_LENGTH_KEY).get();
                if (count == null) {
                    count = 0;
                }
                ByteBuf msg = (ByteBuf) obj;
                if (count + msg.readableBytes() > length) {
                    if (count < length) {
                        ByteBuf byteBuf = msg.readSlice(length - count);
                        randomAccessFile.getChannel().write(byteBuf.nioBuffer());
                        count = length;
                        if (msg.readableBytes() == 0) {
                            byteBuf.release();
                        }
                    }
                    if (msg.readableBytes() == 6) {
                        String msgStr = msg.toString(CharsetUtil.UTF_8);
                        if (msgStr.equals("end\001\001\001")) {
                            ctx.channel().attr(CURRENT_FILE_LENGTH_KEY).set(count);
                            ctx.fireChannelRead(msg);
                            return;
                        }
                    }
                }else{
                    count += msg.readableBytes();
                    randomAccessFile.getChannel().write(msg.nioBuffer());
                    msg.release();
                }
                System.out.println(count);
                ctx.channel().attr(CURRENT_FILE_LENGTH_KEY).set(count);
            }
        }else{  //不是写文件，相当于是数据的字符串，直接交给后面处理
            ctx.fireChannelRead(obj);
        }
    }
}
