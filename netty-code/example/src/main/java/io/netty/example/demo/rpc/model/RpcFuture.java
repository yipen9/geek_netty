package io.netty.example.demo.rpc.model;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class RpcFuture extends FutureTask {
    private Response response;
    public RpcFuture(Callable callable) {
        super(callable);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
