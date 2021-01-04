package com.yipeng.rpc.model;

public class LogMessage extends RPCMessage<String> {
    public static String type = "log";

    public LogMessage() {
        requestType = type;
    }

    @Override
    public Class<String> getMessageBodyDecodeClass(String requestType) {
        return String.class;
    }


    @Override
    public String toString() {
        return getRequestId() + " " + messageBody;
    }
}
