package com.yrpc.client;

import io.netty.util.concurrent.Future;

import java.util.concurrent.Executor;

public interface ListenableFuture<V> extends Future<V> {
    //
    void addListener(Runnable listener, Executor executor);

    void addListener(Runnable listener);

}