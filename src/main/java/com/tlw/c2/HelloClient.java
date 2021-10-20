package com.tlw.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        new Bootstrap()
                //2.添加EventLoop
                .group(new NioEventLoopGroup())
                //3.选择客户端channel实现
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override  //8.连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());  //字符串->ByteBuf
                    }
                })
                //5.连接服务器
                .connect(new InetSocketAddress("localhost",8080))
                .sync()  //6.阻塞方法，直到连接建立
                .channel()  //代表连接对象
                //7.向服务器发送数据
                .writeAndFlush("hello,world");
    }
}
