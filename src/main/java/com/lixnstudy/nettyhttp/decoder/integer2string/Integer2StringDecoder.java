package com.lixnstudy.nettyhttp.decoder.integer2string;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName Integer2StringDecoder
 * @CreateDate 2021/9/9
 * @Description
 */
public class Integer2StringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    public void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) {
        out.add(String.valueOf(msg));
    }
}
