package com.lixnstudy.nettyhttp.jsontransport;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @Author lixn
 * @ClassName JsonSendClient
 * @CreateDate 2021/9/10
 * @Description
 */
public class JsonSendClient {
    static String content = "needed content";
    Bootstrap b = new Bootstrap();
    String serverIP = "";
    int serverPort = 1;

    public void runClient() {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(serverIP, serverPort);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture futureListener) -> {
               if (futureListener.isSuccess()) {
                   System.out.println("EchoClient客户端连接成功");
               } else {
                   System.out.println("EchoClient客户端连接失败");
               }
            });
            // 阻塞，直到连接成功
            f.sync();
            Channel channel = f.channel();
            for (int i = 0; i < 100; i++) {
                JsonMsg user = build(i, i + "->" + content);
                channel.writeAndFlush(user);
                System.out.println("发送报文：" + user.convertToJson());
            }
            channel.flush();
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 从容关闭
            worker.shutdownGracefully();
        }
    }

    private static JsonMsg build(int i, String content) {
        return new JsonMsg(i, content);
    }
}
