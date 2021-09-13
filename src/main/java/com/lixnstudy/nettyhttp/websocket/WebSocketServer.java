package com.lixnstudy.nettyhttp.websocket;

import com.lixnstudy.nettyhttp.discard.NettyDiscardHandler;
import com.lixnstudy.nettyhttp.discard.NettyDiscardServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author lixn
 * @ClassName WebSocketServer
 * @CreateDate 2021/9/13
 * @Description
 */
public class WebSocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
    public void runServer(int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(port);
            bootstrap.childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new HttpServerCodec());// 解码成HttpRequest
                    pipeline.addLast(new HttpObjectAggregator(1024*10));// 解码成FullHttpRequest
//                    pipeline.addLast(new WebSocketServerProtocolHandler("/")); // 添加WebSocket解编码
                    pipeline.addLast(new NettyDiscardHandler());// 自定义解码器
                }
            });

            ChannelFuture connect = bootstrap.bind();
            connect.sync();
            LOGGER.info("webSocket服务器成功启动");
//            ChannelFuture close = connect.channel().close().sync();

            ChannelFuture close = connect.channel().closeFuture();
            close.sync();
        } catch (InterruptedException e) {
            LOGGER.info("初始化服务器出错");
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        WebSocketServer server = new WebSocketServer();
        server.runServer(8080);
    }
}
