package com.lixnstudy.demo.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author lixn
 * @ClassName StartHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class StartHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("我是start ： " + System.currentTimeMillis());
        ctx.fireChannelRead(msg);
    }
}
