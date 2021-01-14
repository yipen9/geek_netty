package com.yipeng.testrpc.handler;

import com.yipeng.testrpc.model.RPCMessage;

public interface Handler {
    public void handle(RPCMessage rpcMessage);
}
