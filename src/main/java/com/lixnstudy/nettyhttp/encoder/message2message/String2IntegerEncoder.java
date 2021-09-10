package com.lixnstudy.nettyhttp.encoder.message2message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName String2IntegerEncoder
 * @CreateDate 2021/9/9
 * @Description
 */
public class String2IntegerEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String s, List<Object> out) throws Exception {
        char[] arrays = s.toCharArray();
        for (char a : arrays) {
            if (a >= 48 && a <= 57) {
                out.add(new Integer(a));
            }
        }
    }
}
