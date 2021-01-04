package com.yipeng.rpc.handler;

import com.yipeng.rpc.model.RPCMessage;

public interface Handler {
    public void handle(RPCMessage rpcMessage);
}
