package com.lixnstudy.udp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName UdpServerHandler
 * @CreateDate 2021/9/14
 * @Description
 */
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    // channelRead调用了channelRead0，channelRead的作用是做一次检查，判断当前message是否需要传到下一个handler
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
//        // 类型转换，将msg转换为ByteBuf
//        ByteBuf buf = packet.copy().content();
//
//        // 新建byte数组
//        byte[] req = new byte[buf.readableBytes()];
//        // 将缓冲区的字节数组复制到byte数组
//        buf.readBytes(req);
//        // todo 处理req，获取请求信息
//        String json = "hello world";
//        // 因为数据包到数据是以字符数据到形式存储到，所以传输数据
//        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
//        DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
//        ctx.writeAndFlush(data);
        // ------------------------------------------------------------------------------------------ //
        String req = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println("捕捉到" + req);

        if ("啪啪啪".equals(req)) {
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("结果：", CharsetUtil.UTF_8),
                    packet.sender()
            ));
        }
    }
}
