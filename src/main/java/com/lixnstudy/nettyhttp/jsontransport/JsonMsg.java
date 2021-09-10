package com.lixnstudy.nettyhttp.jsontransport;

import com.lixnstudy.nettyhttp.JsonUtil;

/**
 * @Author lixn
 * @ClassName JsonMsg
 * @CreateDate 2021/9/10
 * @Description
 */
public class JsonMsg {
    private int id;
    private String content;

    public JsonMsg(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public String convertToJson() {
        return JsonUtil.pojoToJson(this);
    }

    public static JsonMsg parseFromJson(String json) {
        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }
}
