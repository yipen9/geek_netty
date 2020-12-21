package com.yipeng.nio;

import io.netty.buffer.ByteBuf;
import javafx.scene.input.DataFormat;
import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileTransportServer {
    static Map<String, TransportStatus> transportMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(7070));
        int bytesize = 0;
        while (true) {
            System.out.println("select .....");
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    TransportStatus transportStatus = new TransportStatus();
                    transportMap.put(socketChannel.toString(), transportStatus);
                    Thread.sleep(2000);
                    socketChannel.write(ByteBuffer.wrap("please input filepath ! ".getBytes()));
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    TransportStatus status = transportMap.get(socketChannel.toString());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    if (status.getFilePath() == null) {
                        int size = socketChannel.read(buffer);
                        buffer.flip();
                        byte[] array = new byte[size];
                        buffer.get(array, 0, size);
                        status.filePath = new String(array);
                        buffer.clear();
                        File f = new File(status.filePath);
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        FileOutputStream outputStream = new FileOutputStream(status.filePath);
                        status.fileChannel = outputStream.getChannel();
                        socketChannel.write(ByteBuffer.wrap("OKKKK".getBytes()));
                    } else if (status.size == 0) {
                        socketChannel.read(buffer);
                        buffer.flip();
                        byte[] array = new byte[8];
                        buffer.get(array, 0, 8);
                        ByteBuffer buffer1 = ByteBuffer.allocate(8);
                        buffer1.put(array);
                        buffer1.flip();
                        status.size = buffer1.getLong();
                        buffer.clear();
                    } else {
                        int readSize = socketChannel.read(buffer);
                        bytesize += readSize;
                        System.out.println(format5(bytesize * 100.0 / status.size) + "%");
                        buffer.flip();
                        if (bytesize < status.size) {
                            status.fileChannel.write(buffer);
                            if (bytesize % (1024 * 1024 * 10) == 0) {
                                status.fileChannel.force(true);
                            }
                            buffer.clear();
                        } else {
                            status.fileChannel.write(buffer);
                            status.fileChannel.force(true);
                            status.fileChannel.close();
                            buffer.clear();
                            socketChannel.write(ByteBuffer.wrap("写入完成 ! ".getBytes()));
                        }
                    }
                }
                iterator.remove();
            }
        }

    }

    static class TransportStatus {
        private FileChannel fileChannel;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long size = 0;
        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        String filePath;

        public FileChannel getFileChannel() {
            return fileChannel;
        }

        public void setFileChannel(FileChannel fileChannel) {
            this.fileChannel = fileChannel;
        }
    }


    public static String format5(double value) {
        return String.format("%.2f", value).toString();
    }


}
