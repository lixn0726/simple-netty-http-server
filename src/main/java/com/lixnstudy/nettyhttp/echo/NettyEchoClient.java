package com.lixnstudy.nettyhttp.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @Author lixn
 * @ClassName NettyEchoClient
 * @CreateDate 2021/9/8
 * @Description
 */
public class NettyEchoClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyEchoClient.class);
    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();
    public NettyEchoClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(serverIp, serverPort);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel channel) throws Exception{
                    channel.pipeline().addLast(new NettyEchoClientHandler());
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) -> {
                if (futureListener.isSuccess()) {
                    logger.info("EchoClient连接成功");
                } else {
                    logger.info("EchoClient连接失败");
                }
            });
            f.sync();// 阻塞，直到连接成功
            Channel channel = f.channel();
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入发送内容：");
            while (scanner.hasNext()) {
                String next = scanner.next();
                byte[] bytes = (System.currentTimeMillis() + ">>" + next).getBytes();
                // 发送ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(bytes);
                System.out.println("请输入发送内容");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ip = "";
        int port = 1;
        NettyEchoClient client = new NettyEchoClient(ip, port);
        client.runClient();
    }
}
