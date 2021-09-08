package com.lixnstudy.nettyhttp.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author lixn
 * @ClassName NettyDiscardServer
 * @CreateDate 2021/9/7
 * @Description
 */
public class NettyDiscardServer {
    // 实现消息的discard 丢弃功能
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDiscardServer.class);
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();// 封装类
    public NettyDiscardServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        // 创建反应器线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);//为什么boss只需要一个线程？
        EventLoopGroup workerLopGroup = new NioEventLoopGroup();
        try {
            // 1 设置反应器线程组
            b.group(bossLoopGroup, workerLopGroup);
            /*
            ps、设置反应器线程组的时候，可以只设置一个worker线程组
            b.group(workerLoopGroup);
            带来的风险就是：新连接的接收被更耗时的数据传输或者业务处理所阻塞
            所以推荐配置两个线程组
             */
            // 2 设置nio类型的通道
            b.channel(NioServerSocketChannel.class);
            /*
            ps、可以设置为BIO/OIO类型的通道
            b.channel(OioServerSocketChannel.class);
            但是一般不会这么做
             */
            // 3 设置监听端口
            b.localAddress(serverPort);
            // 4 设置通道的参数
            /*
            这里的参数是给父通道配置的，对应bossLoopGroup
            如果要给子通道进行配置，调用
            b.childOption();
             */
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 5 装配子通道流水线
            /*
            为什么不需要装配父通道的流水线？
                因为父通道的内部业务处理是固定的：
                接收新连接 -> 创建子通道 -> 初始化子通道
            如果真的需要自定义，可以使用
            b.handler(ChannelHandler handler);
             */
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                // ChannelInitializer::initChannel(Channel channel)
                // 接收一个Channel通道，拿到这个通道的流水线Pipeline，往里面装配Handler
                // ------------------------------ //
                // 有连接到达时会创建一个通道
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 流水线管理字通道中的Handler处理器
                    // 向子通道流水线添加一个Handler处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });
            // 6 开始绑定服务器
            // 通过调用sync同步方法阻塞直到绑定成功
            /*
            在Netty中，所有IO操作都是异步执行的，那么任何一个IO操作都会立刻返回，在返回的时候，任务还没有真正的执行
            那么什么时候执行完成呢？
            Netty的异步IO都会返回ChanelFuture，
                1、通过自我阻塞一直到ChannelFuture异步我任务执行完成，
                2、或者为ChannelFuture增加事件监听器
            两种方式，使用了第一种
             */
            ChannelFuture channelFuture = b.bind().sync();
            LOGGER.info("服务器启动成功，监听端口" +
                    channelFuture.channel().localAddress());
            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 关闭EventLoopGroup
            // 释放掉所有资源包括创建的线程
            workerLopGroup.shutdownGracefully();// gracefully 优雅
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new NettyDiscardServer(port).runServer();// 启动
    }
}
