package com.yipeng.rpc.model;

public class YiData {
    String data;
    short version;
    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "YiData{" +
                "data='" + data + '\'' +
                ", version=" + version +
                '}';
    }

    public void setData(String data) {
        this.data = data;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }


}
