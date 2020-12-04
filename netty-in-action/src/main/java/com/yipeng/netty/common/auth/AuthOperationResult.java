package com.yipeng.netty.common.auth;

import com.yipeng.netty.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {
    private final boolean passAuth;
}
