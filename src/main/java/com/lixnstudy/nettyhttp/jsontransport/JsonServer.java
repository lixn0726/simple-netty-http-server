package com.lixnstudy.nettyhttp.jsontransport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 * @Author lixn
 * @ClassName JsonServer
 * @CreateDate 2021/9/10
 * @Description
 */
public class JsonServer {
    public void runServer() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker);
            b.channel(NioServerSocketChannel.class);
//            b.localAddress();
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4)
                    );
                    ch.pipeline().addLast(
                            new StringDecoder(Charset.forName("UTF-8"))
                    );
                    ch.pipeline().addLast(new JsonMsgDecoder());
                }
            });
            // todo 端口绑定 服务监听 从容关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class JsonMsgDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String json = (String) msg;
            JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
            System.out.println("收到一个Json数据包 =》 " + jsonMsg);
        }
    }


}
