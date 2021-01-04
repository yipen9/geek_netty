package com.yipeng.lamda;

import java.util.function.Consumer;

public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        Consumer<String> consumer = (String x) -> System.out.println(x);
        consumer.accept("123");

    }
}
