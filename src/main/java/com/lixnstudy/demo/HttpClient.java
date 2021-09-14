package com.lixnstudy.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @Author lixn
 * @ClassName HttpClient
 * @CreateDate 2021/9/14
 * @Description
 */
public class HttpClient {
    public static void main(String[] args) throws Exception{
        HttpClient client = new HttpClient();
        client.start();
    }

    public void start() throws Exception{
        CloseableHttpClient client = HttpClientBuilder.create().build();
        for (int i = 0; i < 100; i++) {
            HttpGet get = new HttpGet("http://127.0.0.1:8080");
            get.setHeader("User-Agent", "'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36'");
            HttpResponse response = client.execute(get);
            System.out.println("第" + i + "次发送请求");
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getEntity().getContent().toString());
        }
    }
}
