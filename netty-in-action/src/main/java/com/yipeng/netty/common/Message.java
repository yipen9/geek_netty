package com.yipeng.netty.common;

import com.yipeng.netty.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.Charset;

@Data
public abstract class Message <T extends MessageBody>{
    private MessageHeader messageHeader;

    private T messageBody;

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(messageHeader.getVersion());
        byteBuf.writeLong(messageHeader.getStreamId());
        byteBuf.writeInt(messageHeader.getOpCode());
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    //根据操作类型获取
    public abstract Class<T> getMessageBodyDecodeClass(int opcode);


    public void decode(ByteBuf message) {
        int version = message.readInt();
        long streamId = message.readLong();
        int opCode = message.readInt();

        MessageHeader header = new MessageHeader();
        header.setVersion(version);
        header.setStreamId(streamId);
        header.setOpCode(opCode);
        this.messageHeader = header;

        Class<T> clazz = getMessageBodyDecodeClass(opCode);
        this.messageBody = JsonUtil.fromJson(message.toString(Charset.forName("utf8")), clazz);

    }
}
