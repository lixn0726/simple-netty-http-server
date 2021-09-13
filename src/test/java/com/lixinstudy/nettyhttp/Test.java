package com.lixinstudy.nettyhttp;

import com.lixnstudy.nettyhttp.decoder.string.replay.RandomUtil;
import com.lixnstudy.nettyhttp.studydemo.InHandlerDemo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.assertj.core.data.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lixn
 * @ClassName Test
 * @CreateDate 2021/9/8
 * @Description
 */
public class Test {
    @org.junit.jupiter.api.Test
    public void testLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);// todo handlerAdded
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);// 通道被创建 -> channelRegistered?
        // 在创建通道的时候，new进去一个ChannelInitializer，在Initializer中，往流水线中增加了一个Handler
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        channel.writeInbound(buf);
        channel.flush();
        buf.writeInt(1);
        channel.writeInbound(buf);
        channel.flush();
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    public void test() {
        System.out.println(RandomUtil.randInMod(3));
    }


    @org.junit.jupiter.api.Test
    public void testListDelete() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "user1"));
        users.add(new User(2, "user2"));
        users.add(new User(3, "user3"));
        users.add(new User(4, "user4"));
        users.add(new User(5, "user5"));
        users.add(new User(6, "user6"));
//        users.remove(new User(3, "user3"));
//        users.add(new User(3, "user33333"));

        int index = 0;
        for (User user : users) {
            if (user.getId() == 3) {
                break;
            }
            index++;
        }

        users.remove(index);
        users.add(new User(3, "user33333"));


//        System.out.println(users.toString());
        for (User user : users) {
            System.out.println(user.toString());
        }
    }


}
