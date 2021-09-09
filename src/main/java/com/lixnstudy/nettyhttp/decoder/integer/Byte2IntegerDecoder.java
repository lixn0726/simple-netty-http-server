package com.lixnstudy.nettyhttp.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName Byte2IntegerDecoder
 * @CreateDate 2021/9/8
 * @Description 负责解码
 */
public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            System.out.println("解码出一个整数:" + i);
            out.add(i);
        }
    }
}
