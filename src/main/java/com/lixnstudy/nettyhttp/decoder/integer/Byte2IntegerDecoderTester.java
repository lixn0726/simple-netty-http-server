package com.lixnstudy.nettyhttp.decoder.integer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

import static java.lang.Integer.MAX_VALUE;

/**
 * @Author lixn
 * @ClassName Byte2IntegerDecoderTester
 * @CreateDate 2021/9/8
 * @Description
 */
public class Byte2IntegerDecoderTester {
    public void testByteToIntegerDecoder() {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                channel.pipeline().addLast(new Byte2IntegerDecoder());
                channel.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeOutbound(buf);
        }
        try {
            Thread.sleep(MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel channel) throws Exception {
                channel.pipeline().addLast(new Byte2IntegerDecoder());
                channel.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeOutbound(buf);
        }
        try {
            Thread.sleep(MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
