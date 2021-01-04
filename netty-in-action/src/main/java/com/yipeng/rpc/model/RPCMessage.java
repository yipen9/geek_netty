package com.yipeng.rpc.model;

import com.yipeng.netty.util.JsonUtil;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.nio.charset.Charset;

public abstract class RPCMessage<T> implements Serializable {
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    private long requestId;
    protected String requestType = "";

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }

    T messageBody;

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeLong(requestId);
        byte[] requests = requestType.getBytes();
        byteBuf.writeInt(requests.length);
        byteBuf.writeBytes(requests);
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    public void decode(ByteBuf byteBuf) {
        Class<T> clazz = getMessageBodyDecodeClass(requestType);
        this.messageBody = JsonUtil.fromJson(byteBuf.toString(Charset.forName("utf8")), clazz);
    }

    //根据操作类型获取
    public abstract Class<T> getMessageBodyDecodeClass(String requestType);
}
