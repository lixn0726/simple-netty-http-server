package com.lixnstudy.myserver;

import com.lixnstudy.myserver.outbound.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.awt.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName HttpClient
 * @CreateDate 2021/9/10
 * @Description
 */
public class HttpClient {
    private static EventLoopGroup worker;
    private static Bootstrap client;
    static {
        client = new Bootstrap();
        worker = new NioEventLoopGroup();
    }

    public void connect(int port, String host) throws Exception {
        try {
            client.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer() {
                        @Override
                        public void initChannel(Channel ch) {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 异步打开连接
            ChannelFuture f = client.connect(host, port).sync();
            // 异步关闭连接
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String host = "127.0.0.1";
        new HttpClient().connect(port, host);
    }
}
