package com.yipeng.netty.util;

import com.google.gson.Gson;

public class JsonUtil {
    public final static Gson GSON = new Gson();

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

}
