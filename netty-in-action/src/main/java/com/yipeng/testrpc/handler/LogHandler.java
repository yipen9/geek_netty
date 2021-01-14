package com.yipeng.testrpc.handler;

import com.yipeng.testrpc.model.RPCMessage;

public class LogHandler implements Handler {

    @Override
    public void handle(RPCMessage rpcMessage) {
        System.out.println(rpcMessage);
    }
}
