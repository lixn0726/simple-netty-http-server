package com.lixnstudy.udp.medium;

import com.lixnstudy.udp.handler.UdpServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @Author lixn
 * @ClassName TestServer
 * @CreateDate 2021/9/14
 * @Description
 */
public class TestServer {
    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        testServer.run(8080);
    }

    public void run(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap server = new Bootstrap();
            server.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpServerHandler());
            ChannelFuture connect = server.bind(port);
            connect.sync();

            ChannelFuture close = connect.channel().closeFuture();
            close.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
