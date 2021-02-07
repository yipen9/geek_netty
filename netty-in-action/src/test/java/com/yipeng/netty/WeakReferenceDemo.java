package com.yipeng.netty;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceDemo {
    private static final ReferenceQueue<Object> dummyQueue = new ReferenceQueue();
    public static void main(String[] args) throws InterruptedException {
        StrData abc = new StrData("abc", dummyQueue);
        abc = null;
        System.gc();
        Thread.sleep(10000);
    }



    static class StrData extends WeakReference<String> {
        String data;
        StrCleaner strCleaner;
        public StrData(String referent, ReferenceQueue q) {
            super(referent, q);
            strCleaner = new StrCleaner(this);
        }

        @Override
        public void clear() {
            strCleaner.run();
        }
    }


    static class StrCleaner implements Runnable{
        StrData strData;

        public StrCleaner(StrData strData) {
            this.strData = strData;
        }

        @Override
        public void run() {
            System.out.println(strData + "clean ...");
        }
    }
}