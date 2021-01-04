package com.yipeng.rpc.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RPCFrameDeCoder extends LengthFieldBasedFrameDecoder {
    public RPCFrameDeCoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
