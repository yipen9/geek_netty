package io.netty.demo.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.List;

import static io.netty.demo.file.RevHandler.*;

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
        if (ctx.channel().hasAttr(REV_FILE_KEY)) {  //如果正在写文件，就写文件。因为可能粘包，文件开始的字节和输入的字符串在一次传输
            Integer count = ctx.channel().attr(CURRENT_FILE_LENGTH_KEY).get();
            if (count == null) {
                count = 0;
            }
            int length = ctx.channel().attr(FILE_LENGTH_KEY).get();
            if (count < length) {
                RandomAccessFile randomAccessFile = ctx.channel().attr(REV_FILE_KEY).get();
                count += msg.readableBytes();
                randomAccessFile.getChannel().write(msg.nioBuffer());
                ctx.channel().attr(CURRENT_FILE_LENGTH_KEY).set(count);
            }else{
                out.add(msg.toString(charset));
            }
        }else{
            out.add(msg.toString(charset)); //解析字符串，给到后面处理
        }
    }


}
