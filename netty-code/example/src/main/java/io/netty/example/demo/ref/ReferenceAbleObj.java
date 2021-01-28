package io.netty.example.demo.ref;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakTracker;

import java.util.concurrent.atomic.AtomicInteger;

public class ReferenceAbleObj extends AbstractReferenceCounted {

    private static ResourceLeakDetector<ReferenceAbleObj> detector =
            new ResourceLeakDetector(ReferenceAbleObj.class, 2);
    ResourceLeakTracker<ReferenceAbleObj> track = null;

    public ReferenceAbleObj() {
        super();
        this.track = detector.track(this);

    }

    //release()调用后，如果refCnt为0， 会执行该方法
    protected void deallocate() {
        System.out.println("开始回收对象自身, 此时的引用数是:" + refCnt());
        if (refCnt() == 0) {
            System.out.println(track);
            track.close(this); //不再追踪
        }
        track = null;
        System.out.println("回收结束");
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        if (track != null) {
            track.record(hint);
        }
        return this;
    }

    public void action1() {
        touch("再次被使用");
    }

    public void action2() {
        touch("再次被使用");
    }


    public static void main(String... args) {
        detector.setLevel(ResourceLeakDetector.Level.PARANOID);
        ReferenceAbleObj obj = new ReferenceAbleObj();
        System.out.println("初始引用数：" + obj.refCnt());
        obj.retain();
        System.out.println("retain后的引用数：" + obj.refCnt());


        obj.touch("第1次被使用");
        obj.touch("第2次被使用");

        obj.action1();
        obj.action2();

        AtomicInteger i = new AtomicInteger(0);
        boolean release1 = obj.release();
        System.out.println("第" + i.addAndGet(1) + "次释放：" + release1 + " " + obj.refCnt());

        System.out.println("第" + i.addAndGet(1) + "次释放即将开始");
        obj.release();

    }


}