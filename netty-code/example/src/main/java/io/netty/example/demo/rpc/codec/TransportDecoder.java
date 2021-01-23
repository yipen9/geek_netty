package io.netty.example.demo.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.demo.rpc.model.TransportModel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TransportDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        TransportModel transportModel = parseByteBuf(in);
        if (transportModel != null) {
            out.add(transportModel);
        }
    }


    private TransportModel parseByteBuf(ByteBuf in) {
        int byteReadable = in.readableBytes();
        //头文件包含 8 + 1 + 4 + 4
        if (byteReadable <= 17) {
            return null;
        }
        long transportId = in.getLong(0);   //获取tansportId，读索引不后移
        byte tranportType = in.getByte(8);
        int dataType = in.getInt(9);
        int dataLength = in.getInt(13);

        if (byteReadable < dataLength + 17) {
            return null;
        }
        TransportModel transportModel = new TransportModel();
        transportModel.decode(in);
        return transportModel;
    }

}
