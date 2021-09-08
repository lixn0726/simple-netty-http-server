package com.lixnstudy.nettyhttp.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author lixn
 * @ClassName NettyEchoClientHandler
 * @CreateDate 2021/9/8
 * @Description
 */
@ChannelHandler.Sharable
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();
    private static final Logger logger = LoggerFactory.getLogger(NettyEchoClientHandler.class);

    /**
     * 出站处理方法
     * @param ctx 上下文
     * @param msg 入站数据包
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        logger.info("client received :" + new String(arr, "UTF-8"));
        //释放ByteBuf的两种方法
        // 方法一：手动释放
        byteBuf.release();
        // 方法二：调用父类的入站方法，向后传递msg
//        super.channelRead(ctx, msg);
    }
}
