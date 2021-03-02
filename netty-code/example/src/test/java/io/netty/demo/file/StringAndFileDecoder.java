package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

import static io.netty.demo.file.RevHandler.REV_FILE_KEY;

@ChannelHandler.Sharable
public class StringAndFileDecoder extends MessageToMessageDecoder<ByteBuf> {

    // TODO Use CharsetDecoder instead.
    private final Charset charset;

    /**
     * Creates a new instance with the current system character set.
     */
    public StringAndFileDecoder() {
        this(Charset.defaultCharset());
    }

    /**
     * Creates a new instance with the specified character set.
     */
    public StringAndFileDecoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (ctx.channel().hasAttr(REV_FILE_KEY)) {
            WriteFileHandler writeFileHandler = (WriteFileHandler) ctx.pipeline().get("WriteFileHandler");
            msg.retain();
            writeFileHandler.channelRead(ctx, msg);
        }else{
            out.add(msg.toString(charset));
        }
    }
}