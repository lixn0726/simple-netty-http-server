package com.lixnstudy.nettyhttp.studydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Author lixn
 * @ClassName WriteReadDemo
 * @CreateDate 2021/9/8
 * @Description
 */
public class WriteReadDemo {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);// i 初始容量， i1 最大限制
        System.out.println("动作：分配ByteBuf(9, 100)");

        buffer.writeBytes(new byte[]{1,2,3,4});

        System.out.println("动作：写入4个字节(1, 2, 3, 4)");
//        System.out.println("start ============ : get ===========");

//        getByteBuf(buffer);
        System.out.println("动作：读取ByteBuf");
        System.out.println("start ============ : read ===========");

        readByteBuf(buffer);

        System.out.println("动作：读完ByteBuf");
    }
    // 取字节，取的时候把指针给改了吧，因为read出来了？
    private static void getByteBuf(ByteBuf buf) {
        while (buf.isReadable()) {
            System.out.println("取一个字节 : " + buf.readByte());// readBytes会把数据读取完，所以后续再get就没有数据了
        }
    }
    // 读字节，不改变指针
    private static void readByteBuf(ByteBuf buf) {
        System.out.println(buf.readableBytes());
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.println("读一个字节 : " + buf.getByte(i));
        }
    }
}
