package com.lixnstudy.nettyhttp.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName Byte2IntegerReplyDecoder
 * @CreateDate 2021/9/8
 * @Description
 */
// ReplayingDecoder可以省去长度的判断
    // 其实就是一个偷梁换柱，在将外部ByteBuf传给子类之前，换成了自己的ReplayingDecoderBuffer
    // ReplayingDecoderBuffer是一个内部类，继承了ByteBuf类型，包装了ByteBuf的大部分读取方法，主要是进行二进制数据长度的判断，
    // 而ReplayingDecoder的作用不仅仅是长度判断，更重要的是用于分包传输的应用场景
public class Byte2IntegerReplyDecoder extends ReplayingDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int i = in.readInt();
        System.out.println("解码出一个整数 : " + i);
        out.add(i);
    }
}
