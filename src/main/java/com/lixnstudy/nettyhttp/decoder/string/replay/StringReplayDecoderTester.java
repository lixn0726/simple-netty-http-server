package com.lixnstudy.nettyhttp.decoder.string.replay;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

import java.nio.charset.Charset;

/**
 * @Author lixn
 * @ClassName StringReplayDecoderTester
 * @CreateDate 2021/9/9
 * @Description
 */
public class StringReplayDecoderTester {
    static final String content = "我是测试用的字符串啊！别认错人了哦～";

    public static void main(String[] args) {
        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) {
                embeddedChannel.pipeline().addLast(new StringReplayDecoder());// 解码
                embeddedChannel.pipeline().addLast(new StringProcessHandler());// 打印
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for (int j = 0; j < 100; j++) {
            // 1 - 3之间的随机数
            int random = RandomUtil.randInMod(2) + 1;
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(bytes.length * random);
            for (int k = 0; k < random; k++) {
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
//    public void testStringReplayDecoder() {
//        ChannelInitializer channelInitializer = new ChannelInitializer<EmbeddedChannel>() {
//            @Override
//            protected void initChannel(EmbeddedChannel embeddedChannel) {
//                embeddedChannel.pipeline().addLast(new StringReplayDecoder());// 解码
//                embeddedChannel.pipeline().addLast(new IntegerProcessHandler());// 打印
//            }
//        };
//        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
//        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
//        for (int j = 0; j < bytes.length; j++) {
//            // 1 - 3之间的随机数
//            int random = RandomUtil.randInMod(3);
//            ByteBuf buf = Unpooled.buffer();
//            for (int k = 0; k < random; k++) {
//                buf.writeBytes(bytes);
//            }
//            channel.writeInbound(buf);
//        }
//        try {
//            Thread.sleep(Integer.MAX_VALUE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
}
