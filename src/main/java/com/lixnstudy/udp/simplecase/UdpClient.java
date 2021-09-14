package com.lixnstudy.udp.simplecase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @Author lixn
 * @ClassName UdpClient
 * @CreateDate 2021/9/14
 * @Description
 */
public class UdpClient {
    public static void main(String[] args) throws IOException {
        // 创建服务端+端口
        DatagramSocket client = new DatagramSocket(614);
        // 准备即将发送的数据
        String msg = "gotta send a message to server";
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        // 数据打包
        DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress("127.0.0.1", 8080));
        // 发送数据
        client.send(packet);
        // 关闭资源
        client.close();
    }
}
