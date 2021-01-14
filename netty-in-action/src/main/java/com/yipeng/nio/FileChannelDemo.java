package com.yipeng.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
//        printlnFile();

        String souce = "/Users/yipeng/tmp/Feishu-3.32.3.dmg";
        String target = "/Users/yipeng/tmp/Feishu-3.32.3111.dmg";
        boolean c = copyFile(souce, target);
        System.out.println(c);
    }


    public static void printlnFile() throws IOException {
        String fileName = "/Users/yipeng/tmp/aaa.txt";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bufSize = 0;
        while ((bufSize = fileChannel.read(buffer)) != -1) {
            String str = new String(buffer.array());
            System.out.println(str);
        }
    }


    public static boolean copyFile(String source, String target) {
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(target);

            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int readSize = 0;
            try{
                while ((readSize = inputChannel.read(buffer)) != -1) {
                    buffer.flip();
                    int outlength = 0;
                    while ((outlength = outChannel.write(buffer)) != 0) {
                        System.out.println("字节数：" + outlength);
                    }
                    buffer.clear();
                }

            }finally {
                inputChannel.close();
                outChannel.force(true);
                outChannel.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
