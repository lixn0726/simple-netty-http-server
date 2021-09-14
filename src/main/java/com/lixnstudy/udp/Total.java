package com.lixnstudy.udp;

/**
 * @Author lixn
 * @ClassName Total
 * @CreateDate 2021/9/14
 * @Description
 */
public class Total {
    /*
    描述所需要用到到消息容器以及Channel类型
    1、interface AddressedEnvelope<M, A extends SocketAddress> extends ReferenceCounted 定义一个消息，包装了另一个消息并带有发送者和接受者地址，M是消息类型，A是地址类型
    2、class DefaultAddressedEnvelope<M, A extends SocketAddress> implements AddressedEnvelope<M, A> 提供了interface的默认实现
    3、class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder 拓展DefaultAddressedEnvelope以使用ByteBuf作为数据容器
    4、interface DatagramChannel extends Channel 拓展了Netty的Channel以支持UDP的多播
    5、class NioDatagramChannel extends AbstractNioMessageChannel implements DatagramChannel 能够发送和接受AddressedEnvelope消息的Channel
     */
}
