package com.tlw.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 零拷贝分片
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});

        //在切片过程中，没有发生数据复制
        ByteBuf f1 = buf.slice(1,5);
        f1.retain();  //计数加1，以免buf释放后报错

        ByteBuf f2 = buf.slice(5,5);
        f2.retain();

        System.out.println("=========================");
        f1.setByte(0,'b');

        buf.release();
        f1.release();
        f2.release();
    }
}
