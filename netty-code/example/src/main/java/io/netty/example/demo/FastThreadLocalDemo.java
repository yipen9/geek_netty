package io.netty.example.demo;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class FastThreadLocalDemo {
    private static final FastThreadLocal<Integer> fastThreadLocal1 = new FastThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() throws Exception {
            return 100;
        }
    };

    private static final FastThreadLocal<String> fastThreadLocal2 = new FastThreadLocal<String>(){
        @Override
        protected String initialValue() throws Exception {
            return "haha";
        }
    };

    public static void main(String[] args) {
        Integer x = fastThreadLocal1.get();
        String s = fastThreadLocal2.get();
        fastThreadLocal1.set(200);

        FastThreadLocalThread fastThreadLocalThread = new FastThreadLocalThread(new Runnable() {
            @Override
            public void run() {
                Integer sss = fastThreadLocal1.get();
                System.out.println(Thread.currentThread() + " " + sss);
            }
        });
        fastThreadLocalThread.start();
        System.out.println(Thread.currentThread() + " " + fastThreadLocal1.get());
        fastThreadLocal2.set("hehe");
    }
}
