package com.lixnstudy.demo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author lixn
 * @ClassName DoNothingHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class DoNothingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
