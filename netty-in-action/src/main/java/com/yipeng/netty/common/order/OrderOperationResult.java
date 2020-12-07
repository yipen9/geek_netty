package com.yipeng.netty.common.order;

import com.yipeng.netty.common.OperationResult;
import lombok.Data;

@Data
public class OrderOperationResult extends OperationResult {
    private final int tableId;
    private final String dish;
    private final boolean complete;

}
