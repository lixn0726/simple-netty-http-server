package com.lixnstudy.nettyhttp.studydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * @Author lixn
 * @ClassName InHandlerDemoTester
 * @CreateDate 2021/9/8
 * @Description
 */
public class InHandlerDemoTester {

    public void testInHandlerLifeCircle() {
        final InHandlerDemo handler = new InHandlerDemo();
        // 初始化处理器
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline().addLast(handler);
            }
        };
        // 创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel();
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        // 模拟入站，写入一个入站数据包
        channel.writeInbound(buf);
        channel.flush();
        // 模拟入站，再写入一个数据报
        channel.writeInbound(buf);
        channel.flush();
        // 通道关闭
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        channel.writeInbound(buf);
        channel.flush();
        buf.writeInt(1);
        channel.writeInbound(buf);
        channel.flush();
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
