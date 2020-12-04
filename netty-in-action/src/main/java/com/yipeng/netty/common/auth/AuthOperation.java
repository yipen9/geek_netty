package com.yipeng.netty.common.auth;

import com.yipeng.netty.common.Operation;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class AuthOperation  extends Operation {
    private final String userName;
    private final String password;

    @Override
    public AuthOperationResult execute() {
        if ("admin".equals(userName) && "123".equals(password)) {
            AuthOperationResult authOperationResult = new AuthOperationResult(true);
            return authOperationResult;
        }
        return new AuthOperationResult(false);
    }
}
