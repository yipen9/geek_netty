package io.netty.demo.rpc.model;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class TransportModel implements Serializable {

    private long transportId;
    private byte transportType;
    private int dataType;
    private byte[] datas;

    public long getTransportId() {
        return transportId;
    }

    public void setTransportId(long transportId) {
        this.transportId = transportId;
    }

    public byte getTransportType() {
        return transportType;
    }

    public void setTransportType(byte transportType) {
        this.transportType = transportType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }


    public TransportModel decode(ByteBuf buf) {
        TransportModel transportModel = new TransportModel();
        long transportId = buf.readLong(); //8
        byte transportType = buf.readByte();//1
        int dataType = buf.readInt();//4
        int length = buf.readInt();//4
        byte[] datas = new byte[length];
        buf.readBytes(datas);
        this.transportId = transportId;
        this.transportType = transportType;
        this.dataType = dataType;
        this.datas = datas;
        return transportModel;
    }

    public ByteBuf encode(ByteBuf byteBuf) {
        byteBuf.writeLong(transportId);
        byteBuf.writeByte(transportType);
        byteBuf.writeInt(dataType);
        byteBuf.writeInt(datas.length);
        byteBuf.writeBytes(datas);
        return byteBuf;
    }

}
