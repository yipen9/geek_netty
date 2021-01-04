package com.yipeng.allocator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Date;

public class ByteBufAllocatorDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;

        //tiny规格内存分配 会变成大于等于16的整数倍的数：这里254 会规格化为256
        ByteBuf byteBuf = alloc.directBuffer(254);

        //读写bytebuf
        byteBuf.writeInt(126);
        System.out.println(byteBuf.readInt());

        //很重要，内存释放
        byteBuf.release();

        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);

        File file = new File("D:\\elasticsearch\\temp.rar");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

        MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());


        System.out.println(map.capacity());
        System.out.println(new Date());
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\elasticsearch\\1111.rar");

        FileChannel writeChannel = fileOutputStream.getChannel();
        buffer.flip();
        writeChannel.write(map);
        map.clear();
        writeChannel.close();
        System.out.println(new Date());
    }
}