package io.netty.example.demo.fastthreadlocal;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class FastThreadLocalDemo {
    public static void main(String[] args) {
        final FastThreadLocal<String> fastThreadLocal = new FastThreadLocal<String>();
        FastThreadLocalThread[] threads = new FastThreadLocalThread[30];
        for (int i = 0; i < 30; i++) {
            if (i == 29) {
                System.out.println(i);
            }
            final String s = i + "";
            threads[i] = new FastThreadLocalThread(new Runnable() {
                @Override
                public void run() {
                    fastThreadLocal.set(s);

                    fastThreadLocal.get();
                }
            });
            threads[i].start();
        }

    }
}


