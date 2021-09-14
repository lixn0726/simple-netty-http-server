package com.lixnstudy.demo.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName HttpServerHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String backContent = "请求被服务器处理了";
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();// 请求对应的服务器api
        String method = request.method().name();// HTTP请求类型

        Content content = new Content(uri, method, backContent);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(content.toString().getBytes(StandardCharsets.UTF_8)));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.writeAndFlush(response);
    }
}

@Data
@AllArgsConstructor
class Content {
    String sourceUri;
    String sourceMethodName;
    String content;

    @Override
    public String toString() {
        return "Content{" +
                "sourceUri='" + sourceUri + '\'' +
                ", sourceMethodName='" + sourceMethodName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
