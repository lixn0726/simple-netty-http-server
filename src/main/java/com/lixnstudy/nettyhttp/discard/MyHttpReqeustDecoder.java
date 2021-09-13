package com.lixnstudy.nettyhttp.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName MyHttpReqeustDecoder
 * @CreateDate 2021/9/13
 * @Description
 */
public class MyHttpReqeustDecoder extends HttpRequestDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        super.decode(ctx, buffer, out);
    }
}
