package com.lixnstudy.nettyhttp.decoder.string.replay;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author lixn
 * @ClassName StringReplayDecoder
 * @CreateDate 2021/9/9
 * @Description 字符串分包解码器
 */
public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    enum Status {
        PARSE_1, PARSE_2;
    }

    private int length;
    private byte[] inBytes;
    // 构造函数需要初始化父类的state
    public StringReplayDecoder() {
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case PARSE_1:
                // 第一步，从装饰器中读取长度
                length = in.readInt();
                inBytes = new byte[length];
                // 读取内容并设置"读断点指针"
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                in.readBytes(inBytes, 0, length);
                out.add(new String(inBytes, "UTF-8"));
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}
