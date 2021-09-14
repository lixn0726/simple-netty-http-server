package com.lixnstudy.test;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName ServerHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 处理输入数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {

            FullHttpRequest req = (FullHttpRequest) msg;

            try {
                // 获取uri
                String uri = req.uri();
                System.out.println("uri is : " + uri);
                // 获取请求体
                ByteBuf buf = req.content();
                String content = buf.toString(StandardCharsets.UTF_8);
                System.out.println("content is : " + content);
                // 获取请求方法
                HttpMethod method = req.method();
                System.out.println("method is : " + method.name());
                // 获取请求头
                HttpHeaders headers = req.headers();
                System.out.println("headers are : " + headers);
                // 根据method执行对应的逻辑
                if (method.equals(HttpMethod.GET)) {
                    // todo
                    Content cont = new Content();
                    cont.setContent("服务器处理了GET请求哦");
                    cont.setUri(uri);
                }

                if (method.equals(HttpMethod.POST)) {
                    Content c = new Content();
                    c.setUri(uri);
                    c.setContent(content);

                    response(ctx, c);
                }
                // todo PUT DELETE
            } finally {
                req.release();
            }
        }
    }

    private void response(ChannelHandlerContext ctx, Content c) {
        // 设置响应
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSONObject.toJSONString(c), StandardCharsets.UTF_8));

        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        // 发送，必须在使用完后关闭通道
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }
}

@Data
class Content {
    String uri;
    String content;
}