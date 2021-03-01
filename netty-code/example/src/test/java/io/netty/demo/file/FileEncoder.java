package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToByteEncoder;

public class FileEncoder extends MessageToByteEncoder<FileRegion> {
    @Override
    protected void encode(ChannelHandlerContext ctx, FileRegion msg, ByteBuf out) throws Exception {
        ctx.writeAndFlush(msg);
    }
}
