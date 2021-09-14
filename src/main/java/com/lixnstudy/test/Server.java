package com.lixnstudy.test;

import com.lixnstudy.nettyhttp.discard.NettyDiscardHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;

/**
 * @Author lixn
 * @ClassName Server
 * @CreateDate 2021/9/14
 * @Description
 */
public class Server {
    public void runServer(int port) {
        ServerBootstrap server = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpRequestDecoder());
                            pipeline.addLast(new HttpObjectAggregator(65535));
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture connect = server.bind();
            connect.sync();

            ChannelFuture close = connect.channel().closeFuture();
            close.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.runServer(8080);
    }

}
