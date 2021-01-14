package com.yipeng.testrpc.codec;

import io.netty.handler.codec.LengthFieldPrepender;

public class RPCFrameEncoder extends LengthFieldPrepender {
    public RPCFrameEncoder() {
        super(8, true);
    }
}
