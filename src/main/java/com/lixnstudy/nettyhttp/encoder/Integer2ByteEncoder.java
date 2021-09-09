package com.lixnstudy.nettyhttp.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author lixn
 * @ClassName Integer2ByteEncoder
 * @CreateDate 2021/9/9
 * @Description
 */
public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
        System.out.println("encoder Integer = " + msg);
    }
}
