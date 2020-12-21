package com.yipeng.netty;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class FileTransportClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        int status = 0;
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("10.248.224.92", 7070));
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);


        if (socketChannel.finishConnect()) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isConnectable()) {
                    System.out.println("connectable");
                }

                if (selectionKey.isReadable()) {
                    if (status == 0) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);

                        String title = new String(buffer.array());
                        System.out.println(title);
                        String path = "./elasticsearch-7.6.2.tar.gz";
                        System.out.println("put : " + path);
                        socketChannel.write(ByteBuffer.wrap(path.getBytes()));
                        status = 1;
                    } else if (status == 1) {
                        ByteBuffer buffer = ByteBuffer.allocate(5);
                        socketChannel.read(buffer);
                        String out = new String(buffer.array());
                        if ("OKKKK".equals(out)) {
                            status = 2;
                        }
                        buffer.clear();
                        File f = new File("/elasticsearch/elasticsearch-7.6.2.tar.gz ");
                        Long size = f.length();
                        System.out.println("file:" + size);
                        buffer = ByteBuffer.allocate(1024);
                        buffer.putLong(size);
                        buffer.flip();
                        socketChannel.write(buffer);
                    }

                    if (status == 2) {
                        System.out.println("write file .....");
                        FileInputStream fileInputStream = new FileInputStream("/elasticsearch/elasticsearch-7.6.2.tar.gz");

                        FileChannel fileChannel = fileInputStream.getChannel();

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        long bytesize = 0;
                        int per = 0;
                        while ((per = fileChannel.read(buffer)) > 0) {
                            bytesize = bytesize + per;
                            System.out.println(bytesize);
                            buffer.flip();

                            //存在写入失败的情况
                            while (buffer.hasRemaining()) {
                                int size = socketChannel.write(buffer);
                                if (size < 0) {
                                    throw new EOFException();
                                }
                                if (size != per) {
                                    System.out.println("size : " + size + "per : " + per);
                                }
                            }
                            buffer.clear();
                        }
                        status = 3;
                    }else if (status == 3) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        buffer.flip();
                        String out = new String(buffer.array());
                        buffer.clear();
                    }
                }
                iterator.remove();
            }
        }
    }
}