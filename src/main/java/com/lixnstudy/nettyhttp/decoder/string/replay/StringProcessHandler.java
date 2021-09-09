package com.lixnstudy.nettyhttp.decoder.string.replay;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author lixn
 * @ClassName StringProcessHandler
 * @CreateDate 2021/9/9
 * @Description
 */
public class StringProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = (String) msg;
        System.out.println(s);
    }
}
