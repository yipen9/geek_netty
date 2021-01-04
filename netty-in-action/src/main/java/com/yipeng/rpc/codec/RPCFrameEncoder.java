package com.yipeng.rpc.codec;

import io.netty.handler.codec.LengthFieldPrepender;

public class RPCFrameEncoder extends LengthFieldPrepender {
    public RPCFrameEncoder() {
        super(2, false);
    }
}
