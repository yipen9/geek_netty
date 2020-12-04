package com.yipeng.netty;


import com.yipeng.netty.common.Message;
import com.yipeng.netty.common.MessageHeader;
import com.yipeng.netty.common.Operation;
import com.yipeng.netty.common.OperationType;

public class RequestMessage extends Message<Operation> {
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

    public RequestMessage() {
    }

    public RequestMessage(Long streamId, Operation operation) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }
}
