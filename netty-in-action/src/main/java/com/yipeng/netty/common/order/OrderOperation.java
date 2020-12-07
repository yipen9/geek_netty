package com.yipeng.netty.common.order;

import com.yipeng.netty.common.Operation;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
public class OrderOperation extends Operation {
    private int tableId;
    private String dish;

    public OrderOperation(int tableId, String dish) {
        this.tableId = tableId;
        this.dish = dish;
    }

    @Override
    public OrderOperationResult execute() {
        System.out.println("order's executing startup with orderRequest: " + toString());
        //execute order logic
        System.out.println("order's executing complete");
        OrderOperationResult orderResponse = new OrderOperationResult(tableId, dish, true);
        return orderResponse;
    }

    @Override
    public String toString() {
        return "OrderOperation{" +
                "tableId=" + tableId +
                ", dish='" + dish + '\'' +
                '}';
    }
}
