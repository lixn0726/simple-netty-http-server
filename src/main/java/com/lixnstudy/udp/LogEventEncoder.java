package com.lixnstudy.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author lixn
 * @ClassName LogEventEncoder
 * @CreateDate 2021/9/14
 * @Description
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc()
                .buffer(file.length + msg.length + 1);
        buf.writeBytes(file);
        buf.writeByte(LogEvent.getSEPARATOR());
        buf.writeBytes(msg);
//        out.add(new DatagramPacket(buf, remoteAddress));
        // todo : add something to out
    }
}
