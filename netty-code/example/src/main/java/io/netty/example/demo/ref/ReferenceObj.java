package io.netty.example.demo.ref;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakTracker;

public class ReferenceObj extends AbstractReferenceCounted {
    private ResourceLeakDetector<ReferenceObj> detector =
            new ResourceLeakDetector<ReferenceObj>(ReferenceObj.class, 2);

    ResourceLeakTracker<ReferenceObj> track = null;


    public ReferenceObj() {
        super();
        this.track = detector.track(this);
    }

    @Override
    protected void deallocate() {   //回收
        if (refCnt() == 0) {
            track.close(this);
        }
    }

    @Override
    public ReferenceCounted touch(Object hint) {    //调试
        if (track != null) {
            track.record(hint);
        }
        return this;
    }
}
