package com.tlw.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture =  new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //异步非阻塞，main发起了调用，真正执行connect是nio线程，若不调用sync方法进行阻塞的话，main线程没等connect执行就获取channel
                .connect(new InetSocketAddress("localhost",8080));  //比如1s之后
        /**
         * 使用sync方法同步处理结果
         */
        channelFuture.sync();  //阻塞当前线程，直到nio线程连接建立完毕
        Channel channel = channelFuture.channel();  //代表连接对象
        log.debug("{}",channel);
        channel.writeAndFlush("hello,sync");

        /**
         * 使用addListener(回调对象)方法异步处理结果，即将后面操作都交给nio线程处理
         */
        /*channelFuture.addListener(new ChannelFutureListener() {
            @Override  //在nio线程连接建立好之后，会调用operationComplete
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}",channel);
                channel.writeAndFlush("hello,addListener");
            }
        });*/
    }
}
