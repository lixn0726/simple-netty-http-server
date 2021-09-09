package com.lixnstudy.nettyhttp.decoder;

import com.lixnstudy.nettyhttp.decoder.string.replay.StringProcessHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.logging.log4j.util.Chars;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ForkJoinPool;

/**
 * @Author lixn
 * @ClassName NettyOpenBoxDecoder
 * @CreateDate 2021/9/9
 * @Description
 */
public class NettyOpenBoxDecoder {
    public static final int VERSION = 100;
    static String content = "广播消息：今天星期四！";

    public void testLengthFieldBasedFrameDecoder() {
    }

    public static void main(String[] args) {
        try {
            final LengthFieldBasedFrameDecoder split =
                    new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                @Override
                protected void initChannel(EmbeddedChannel channel) {
                    channel.pipeline().addLast(split);
                    channel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    channel.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);
            for (int j = 1; j <= 100; j++) {
                ByteBuf buf = Unpooled.buffer();
                String s = j + "次发送 -> " + content;
                byte[] bytes = s.getBytes("UTF-8");
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
