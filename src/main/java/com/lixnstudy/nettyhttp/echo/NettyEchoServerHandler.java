package com.lixnstudy.nettyhttp.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author lixn
 * @ClassName NettyEchoServerHandler
 * @CreateDate 2021/9/8
 * @Description
 */
// 该注解表示一个Handler实例可以被多个通道安全地共享
@ChannelHandler.Sharable
// 入站处理器，在可读IO事件到来的时候，被流水线回调
// 入站后第一个入站处理器的channelRead方法的msg类型一定是ByteBuf，但是在经过多个Handler处理后，可能就不是ByteBuf的类型了
// 因为它是Netty读取到的ByteBuf包
// Netty4.1之后，默认ByteBuf类型为DirectByteBuf，所以要经过getBytes、readBytes等方法处理后，将数据读取到Java数组后才能进行处理
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    /*
    逻辑分为两步
        1、获取channelRead的msg参数
        2、调用writeAndFlush写回客户端
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyEchoServerHandler.class);
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;// 转换成Netty的ByteBuf进行处理
        logger.info("msg type :" + (buf.hasArray()?"堆内存":"直接内存"));
        int len = buf.readableBytes();
        byte[] array = new byte[len];
        logger.info("server received : " + new String(array, "UTF-8"));
        logger.info("写回前, msg.refCnt : " + ((ByteBuf) msg).refCnt());// reference count -> refCnt
        // 写回数据，异步任务
        ChannelFuture f = ctx.writeAndFlush(msg);// writeAndFlush 写回给客户端
        f.addListener((ChannelFutureListener) -> {
            logger.info("写回后, msg.refCnt : " + ((ByteBuf) msg).refCnt());
        });
    }
}
// 同一个通道上的所有业务处理器Handler，只能被同一个线程处理。
// 如果是被@Sharable注解了的，可能会有线程安全问题。可以通过isSharable来判断是否被注解presented
// question: 为什么程序运行完后，ByteBuf的引用计数的值变为0，但是没有释放内存的代码，为什么引用计数没有了呢？