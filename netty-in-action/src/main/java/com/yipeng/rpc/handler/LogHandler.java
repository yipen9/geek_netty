package com.yipeng.rpc.handler;

import com.yipeng.rpc.model.RPCMessage;

public class LogHandler implements Handler {

    @Override
    public void handle(RPCMessage rpcMessage) {
        System.out.println(rpcMessage);
    }
}
