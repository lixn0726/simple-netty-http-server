package com.lixnstudy.nettyhttp.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author lixn
 * @ClassName EchoServer
 * @CreateDate 2021/9/8
 * @Description
 */
public class EchoServer {
    public void runServer() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(boss, worker);
        // todo 配置通道类型、通道选项
//        server.option()
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
            }
        });
        // ... todo 启动、等待、从容关闭等
    }

    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer();
        echoServer.runServer();
    }
}
