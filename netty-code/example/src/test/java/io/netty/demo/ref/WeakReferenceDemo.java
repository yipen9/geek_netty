package io.netty.demo.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakReferenceDemo {
    public static void main(String[] args) throws InterruptedException {
//        创建一个引用队列
        ReferenceQueue queue = new ReferenceQueue();

// 创建弱引用，此时状态为Active，并且Reference.pending为空，当前Reference.queue = 上面创建的queue，并且next=null
        WeakClass reference = new WeakClass(new String("1234"), queue);
        System.out.println(reference.get());
// 当GC执行后，由于是弱引用，所以回收该object对象，并且置于pending上，此时reference的状态为PENDING
        System.gc();

        /* ReferenceHandler从pending中取下该元素，并且将该元素放入到queue中，此时Reference状态为ENQUEUED，Reference.queue = ReferenceENQUEUED */

        /* 当从queue里面取出该元素，则变为INACTIVE，Reference.queue = Reference.NULL */
        WeakClass reference1 = (WeakClass) queue.remove();
        System.out.println(reference1.get());       //reference的String对象被回收了

        System.out.println(reference1.name);        //WeakClass对象被放入到queue里面。
    }


    static class WeakClass extends WeakReference<String>{
        String name = null;
        public WeakClass(String referent) {
            super(referent);
        }

        public WeakClass(String referent, ReferenceQueue<? super String> q) {
            super(referent, q);
            name = new String(referent);
        }
    }
}