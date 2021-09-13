package com.lixnstudy.nettyhttp.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author lixn
 * @ClassName NettyDiscardHandler
 * @CreateDate 2021/9/8
 * @Description
 */
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    /*
    引入新概念：入站和出站
        入站 -> 输入
        出站 -> 输出
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyDiscardHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {// 读取了Netty的输入数据缓冲区ByteBuf，其实可以跟NIO的缓冲区对应，不过Netty的性能会更好
        // msg是从底层操作系统的内核缓冲区复制到ByteBuf的？
        ByteBuf in = (ByteBuf) msg;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            LOGGER.info("收到消息如下");
            while (in.isReadable()) {
                char c = (char) in.readByte();
                stringBuilder.append((char) in.readByte());
            }
            String str = stringBuilder.toString();
            String[] request = str.split("\r\n");
            System.out.println("request size is " + request.length);
//            for (String part : request) {
//                System.out.println(part);
//            }
            System.out.println(request[request.length - 1]);
//            System.out.println(stringBuilder.toString());
            System.out.println();

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
