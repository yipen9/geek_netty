package io.netty.demo.ref;


import sun.misc.Cleaner;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class CleanerDemo {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<String> dummyQueue = new ReferenceQueue();
        StrData abc = new StrData(new String("abc"), dummyQueue);
        System.gc();
        Thread.sleep(10000);
    }



    static class StrData extends WeakReference<String> {
        String data;
        StrCleaner strCleaner;
        public StrData(String referent, ReferenceQueue q) {
            super(referent, q);
            strCleaner = new StrCleaner(this);
            Cleaner cleaner = Cleaner.create(referent, strCleaner); //生成cleaner操作，ReferenceHandler,会执行此Runnable方法
        }
    }


    static class StrCleaner implements Runnable{
        StrData strData;

        public StrCleaner(StrData strData) {
            this.strData = strData;
        }

        @Override
        public void run() {
            System.out.println(strData.data + " clean ...");
        }
    }
}

