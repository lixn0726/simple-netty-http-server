package com.lixnstudy.nettyhttp;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;

/**
 * @Author lixn
 * @ClassName JsonUtil
 * @CreateDate 2021/9/9
 * @Description
 */
public class JsonUtil {
    // 谷歌GsonBuilder构造器
    static GsonBuilder gsonBuilder = new GsonBuilder();
    static {
        gsonBuilder.disableHtmlEscaping();
    }

    // Gson序列化
    public static String pojoToJson(Object obj) {
        String json = gsonBuilder.create().toJson(obj);
        return json;
    }

    // FastJson反序列化
    public static <T> T jsonToPojo(String json, Class<T> tClass) {
        T t = JSONObject.parseObject(json, tClass);
        return t;
    }
}
