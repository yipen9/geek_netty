package com.yipeng.testrpc.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RPCFrameDeCoder extends LengthFieldBasedFrameDecoder {
    public RPCFrameDeCoder() {
        super(Integer.MAX_VALUE, 0, 8, -8, 8);
    }
}
