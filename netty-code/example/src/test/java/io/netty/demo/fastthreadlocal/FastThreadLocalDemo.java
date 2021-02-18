package io.netty.demo.fastthreadlocal;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import org.junit.Test;

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

    /**
     * 测试扩容
     */
    @Test
    public void testExpandIndexedVariableTableAndSet() throws InterruptedException {
        FastThreadLocalThread thread = new FastThreadLocalThread(new Runnable() {
            @Override
            public void run() {
                int length = 32;
                FastThreadLocal[] fastThreadLocals = new FastThreadLocal[length];
                for (int i = 0; i < length; i++) {
                    fastThreadLocals[i] = new FastThreadLocal();
                    if (i == length - 1) {
                        System.out.println("debug");
                    }
                    fastThreadLocals[i].set(i);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(100000);
    }
}


