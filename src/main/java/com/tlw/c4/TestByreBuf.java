package com.tlw.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class TestByreBuf {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();  //默认256，容量不够时
        System.out.println(buf.getClass());
        System.out.println(buf.capacity());

        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < 300;i++){
            sb.append("a");
        }
        buf.writeBytes(sb.toString().getBytes());
        System.out.println(buf.capacity());
    }
}
