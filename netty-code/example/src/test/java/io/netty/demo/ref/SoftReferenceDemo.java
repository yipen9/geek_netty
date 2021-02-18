package io.netty.demo.ref;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class SoftReferenceDemo {
    /**
     * 如果一个对象具有软引用，内存空间足够，垃圾回收器就不会回收它；
     * 如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。
     */
    private static List<SoftReference> list = new ArrayList<SoftReference>();
    public static void main(String[] args) {
        testSoftReference();
    }
    private static void testSoftReference() {
//        byte[] buff = null;
        for (int i = 0; i < 10; i++) {
            byte[] buff = new byte[1024 * 1024];
//            buff = new byte[1024 * 1024];
            SoftReference<byte[]> sr = new SoftReference<byte[]>(buff);
            list.add(sr);
        }

        System.gc(); //主动通知垃圾回收

        for(int i=0; i < list.size(); i++){
            Object obj = list.get(i).get();
            System.out.println(obj);
        }


    }
}
