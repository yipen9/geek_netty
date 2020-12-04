package com.yipeng.netty.common;

public abstract class Operation extends MessageBody {
    public abstract OperationResult execute();
}
