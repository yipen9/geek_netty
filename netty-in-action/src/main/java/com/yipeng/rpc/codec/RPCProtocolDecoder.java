package com.yipeng.rpc.codec;

import com.yipeng.rpc.model.LogMessage;
import com.yipeng.rpc.model.RPCMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RPCProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        long requestId = in.readLong();
        int typeLength = in.readInt();
        byte[] byteArray = new byte[typeLength];
        in.readBytes(byteArray);
        String requestType = new String(byteArray);

        if (requestType.equals(LogMessage.type)) {
            LogMessage logMessage = new LogMessage();
            logMessage.setRequestId(requestId);
            logMessage.decode(in);
            out.add(logMessage);
        }
    }
}
