package com.yipeng.netty.common;

import com.yipeng.netty.common.auth.AuthOperation;
import com.yipeng.netty.common.auth.AuthOperationResult;
import com.yipeng.netty.common.keepalive.KeepaliveOperation;
import com.yipeng.netty.common.keepalive.KeepaliveOperationResult;
import com.yipeng.netty.common.order.OrderOperation;
import com.yipeng.netty.common.order.OrderOperationResult;

import java.util.function.Predicate;

public enum OperationType {
    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE(2, KeepaliveOperation.class, KeepaliveOperationResult.class),
    ORDER(3, OrderOperation.class, OrderOperationResult.class),;

    private int opCode;
    private Class<? extends Operation> operationClazz;
    private Class<? extends OperationResult> operationResultClazz;

    OperationType(int opCode, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.opCode = opCode;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }


    public static OperationType fromOpCode(int opCode) {
        return getOperationType(requestType -> requestType.opCode == opCode);
    }

    public static OperationType fromOperation(Operation operation) {
        return getOperationType(requestType -> requestType.operationClazz == operation.getClass());
    }

    private static OperationType getOperationType(Predicate<OperationType> predicate) {
        OperationType[] values = values();
        for (OperationType operationType : values) {
            if (predicate.test(operationType)) {
                return operationType;
            }
        }
        throw new AssertionError("no found type");
    }

    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }
}
