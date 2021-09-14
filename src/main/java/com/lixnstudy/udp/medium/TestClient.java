package com.lixnstudy.udp.medium;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @Author lixn
 * @ClassName TestClient
 * @CreateDate 2021/9/14
 * @Description
 */
public class TestClient {
    public void run(int port)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpClientHandler());

            Channel ch = b.bind(0).sync().channel();
            // 向网段类所有机器广播发UDP
            ch.writeAndFlush(
                    new DatagramPacket(
                            Unpooled.copiedBuffer("啪啪啪", CharsetUtil.UTF_8),
                            new InetSocketAddress(
                                    "255.255.255.255",port
                            ))).sync();
            if(!ch.closeFuture().await(15000)){
                System.out.println("查询超时");
            }
        }
        finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new TestClient().run(8080);
    }
}
