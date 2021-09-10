package com.lixnstudy.myserver;

import com.lixnstudy.myserver.inbound.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName Server
 * @CreateDate 2021/9/10
 * @Description
 */
public class HttpServer {
    private static final ServerBootstrap server;
    private static EventLoopGroup boss;
    private static EventLoopGroup worker;
    private final int port;// 需要监听的端口

    public HttpServer(int port) {
        this.port = port;
    }

    static {
        server = new ServerBootstrap();
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();
    }

    public void runServer() {
        try {
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(this.port)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture connectFuture = server.bind().sync();// 阻塞直到成功连接
            System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*= 服务器成功启动，正在监听端口：" + this.port +" *=*=*=*=*=*=*=*=*=*=*=*=*=");

            ChannelFuture closeFuture = connectFuture.channel().closeFuture();
            closeFuture.sync();
            System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*= 服务器成功关闭 *=*=*=*=*=*=*=*=*=*=*=*=*=");
        } catch (Exception e) {
            System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*= 服务器启动或关闭时出现异常 *=*=*=*=*=*=*=*=*=*=*=*=*=");
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer {
        @Override
        protected void initChannel(Channel channel) throws Exception {
            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes(StandardCharsets.UTF_8));
            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            channel.pipeline().addLast(new StringDecoder());
            channel.pipeline().addLast(new EchoServerHandler());
        }
    }



}
