package com.yipeng.netty.common.keepalive;

import com.yipeng.netty.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {
    private final long time;
}
