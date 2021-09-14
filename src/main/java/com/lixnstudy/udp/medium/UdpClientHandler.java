package com.lixnstudy.udp.medium;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @Author lixn
 * @ClassName UdpClientHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);

        if(response.startsWith("结果：")){
            System.out.println(response);
            ctx.close();
        }
    }
}