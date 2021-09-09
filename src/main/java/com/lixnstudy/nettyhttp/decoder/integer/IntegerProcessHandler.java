package com.lixnstudy.nettyhttp.decoder.integer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author lixn
 * @ClassName IntegerProcessHandler
 * @CreateDate 2021/9/8
 * @Description 负责输出解码后的数据
 */
public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer) msg;
//        super.channelRead(ctx, msg);
        System.out.println("打印出一个整数:" + integer);
    }
}
