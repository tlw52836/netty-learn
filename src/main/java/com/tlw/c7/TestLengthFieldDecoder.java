package com.tlw.c7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * LengthFieldBasedFrameDecoder(lengthFieldOffset,engthFieldLength,lengthAdjustment,initialBytesToStrip)
 * lengthFieldOffset:长度字段偏移量
 * lengthFieldLength:长度字段长度
 * lengthAdjustment:长度字段为基准，还有几个字节是内容
 * initialBytesToStrip:从头剥离几个字节
 */
public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,1,4),
                new LoggingHandler(LogLevel.DEBUG)
        );
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        setBuf(buf,"you are gracefully");
        setBuf(buf,"you never know it");
        channel.writeInbound(buf);
    }
    public static void setBuf(ByteBuf buffer,String content){
        int length = content.length();  //内容长度：占用4个字节
        byte[] bytes = content.getBytes();  //实际内容

        buffer.writeInt(length);
        buffer.writeByte(1);  //模拟协议版本号
        buffer.writeBytes(bytes);
    }
}
