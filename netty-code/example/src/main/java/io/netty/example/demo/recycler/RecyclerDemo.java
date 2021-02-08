package io.netty.example.demo.recycler;
import io.netty.example.demo.ref.ReferenceObj;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakTracker;

import java.util.concurrent.atomic.AtomicInteger;

public class RecyclerDemo {
    /**
     * 在get时，当前线程，会创建一个FastThreadLocal<Stack<T>>的stack
     */
    Recycler<RefObj> recycler = new Recycler<RefObj>() {    //对象池
        @Override
        protected RefObj newObject(Handle<RefObj> handle) {
            return new RefObj(handle);          //创建一个实例时，当前线程
        }
    };


    public static void main(String[] args) throws InterruptedException {
        final RecyclerDemo demo = new RecyclerDemo();
        final RefObj refObj = demo.recycler.get();
        System.out.println("1" + refObj);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                refObj.recycle();   //别的线程释放，会放到weakOrderQueue
            }
        });

        refObj.retain();
        thread.start();
        Thread.sleep(500);
        RefObj refObj1 = demo.recycler.get();   //可以共享
        refObj1.retain();
        refObj1.recycle();
        System.out.println("2" + refObj1);

        thread = new Thread(new Runnable() {    //其他线程在获取。
            @Override
            public void run() {
                RefObj ref = demo.recycler.get();
                System.out.println("3" + ref);  //新线程，生成新的对象
            }
        });

        RefObj ref = demo.recycler.get(); //自己线程在获取去，会将weakOrderQueue 转换为stack
        System.out.println("4" + ref);  //可以共享
        thread.start();



    }


    static class RefObj extends AbstractReferenceCounted{
        public final static AtomicInteger i = new AtomicInteger(0);
        private ResourceLeakDetector<RefObj> detector =
                new ResourceLeakDetector<RefObj>(RefObj.class, 2);

        ResourceLeakTracker<RefObj> track = null;
        private final Recycler.Handle<RefObj> recyclerHandle;


        public RefObj(Recycler.Handle<RefObj> recyclerHandle) {
            i.addAndGet(1);
            this.recyclerHandle = recyclerHandle;
            this.track = detector.track(this);
        }


        @Override
        protected void deallocate() {
            if (refCnt() == 0) {
                track.close(this);
            }
        }

        @Override
        public String toString() {
            return "refObj-" + i.get();
        }

        @Override
        public ReferenceCounted touch(Object hint) {
            if (hint != null) {
                track.record(hint);
            }
            return this;
        }

        private void recycle() {    //回收这个对象
            recyclerHandle.recycle(this);
        }
    }
}
