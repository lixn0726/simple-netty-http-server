package com.lixnstudy.nettyhttp.decoder.string.byte2message;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName StringIntegerHeaderDecoder
 * @CreateDate 2021/9/9
 * @Description
 */
public class StringIntegerHeaderDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        // 可读字节小于4，消息头还没读满，返回
        if (buf.readableBytes() < 4) {
            return;
        }
        // 消息头完整
        // 在真正开始从缓冲区读取数据之前，先调用markReaderIndex() 设置回滚点
        buf.markReaderIndex();
        int length = buf.readInt();
        // 从缓冲区读出消息头的大小。这会使得readIndex读指针前移
        // 剩余长度不够消息题，重置读指针
        if (buf.readableBytes() < length) {
            // 读指针回滚到消息头读readIndex，为进行状态的保存
            buf.resetReaderIndex();
            return;
        }
        // 读取数据，编码成字符串
        byte[] inBytes = new byte[length];
        buf.readBytes(inBytes, 0, length);
        out.add(new String(inBytes,  "UTF-8"));
    }
}
