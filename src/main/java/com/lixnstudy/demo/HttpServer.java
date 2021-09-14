package com.lixnstudy.demo;

import com.lixnstudy.demo.handler.DoNothingHandler;
import com.lixnstudy.demo.handler.HttpServerHandler;
import com.lixnstudy.demo.handler.StartHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @Author lixn
 * @ClassName HttpServer
 * @CreateDate 2021/9/14
 * @Description
 */
public class HttpServer {
    public void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();// 对应父通道 监听连接
        EventLoopGroup worker = new NioEventLoopGroup();// 对应子通道 处理数据
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .localAddress(port)
                    .channel(NioServerSocketChannel.class)// 配置通道类型：NIO/BIO/AIO/...
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // server端发送端是httpResponse，进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // 打印start时间
                            ch.pipeline().addLast(new StartHandler());
                            // server端收到的是httpRequest，需要进行解码并聚合成一条完整的信息
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpObjectAggregator(65535));
                            ch.pipeline().addLast(new DoNothingHandler());
                            // 打印end时间，处理并返回response
                            ch.pipeline().addLast(new HttpServerHandler());
                        }
                    });
            ChannelFuture f = serverBootstrap.bind();
            f.sync();

            ChannelFuture b = f.channel().closeFuture();
            b.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.start(8080);
    }
}
