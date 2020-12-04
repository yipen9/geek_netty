package com.yipeng.netty.common.keepalive;

import com.yipeng.netty.common.Operation;

public class KeepaliveOperation extends Operation {
    private long time ;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public KeepaliveOperationResult execute() {
        KeepaliveOperationResult keepaliveOperationResult = new KeepaliveOperationResult(time);
        return keepaliveOperationResult;
    }
}
