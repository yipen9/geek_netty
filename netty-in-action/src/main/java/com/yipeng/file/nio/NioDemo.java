package com.yipeng.file.nio;


import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioDemo {
    public static void main(String[] args){
        try {
            FileInputStream fileInputStream = new FileInputStream("");
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            fileChannel.read(buffer);
            buffer.flip();
            System.out.println(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
