package com.lixnstudy.udp.simplecase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @Author lixn
 * @ClassName UdpServer
 * @CreateDate 2021/9/14
 * @Description
 */
/*
相比于TCP，因为UDP不需要建立连接，所以不需要为ChannelPipeline设置handler
 */
public class UdpServer {
    public static void main(String[] args) throws Exception{
        // 创建服务端+端口
        DatagramSocket server = new DatagramSocket(8080);
        // 准备接收容器
        byte[] container = new byte[1024];
        // 封装成包
        DatagramPacket packet = new DatagramPacket(container, container.length);
        // 接收数据
        server.receive(packet);
        // 分析数据
        byte[] data = packet.getData();
        int length = packet.getLength();
        String msg = new String(data, 0, length);
        System.out.println(msg);
        server.close();
    }
}
