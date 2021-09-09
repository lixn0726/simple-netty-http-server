package com.lixinstudy.nettyhttp;

import com.lixnstudy.nettyhttp.decoder.string.replay.RandomUtil;
import com.lixnstudy.nettyhttp.studydemo.InHandlerDemo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * @Author lixn
 * @ClassName Test
 * @CreateDate 2021/9/8
 * @Description
 */
public class Test {
    @org.junit.jupiter.api.Test
    public void testLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);// todo handlerAdded
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);// 通道被创建 -> channelRegistered?
        // 在创建通道的时候，new进去一个ChannelInitializer，在Initializer中，往流水线中增加了一个Handler
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

    @org.junit.jupiter.api.Test
    public void test() {
        System.out.println(RandomUtil.randInMod(3));
    }

}
