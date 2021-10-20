package com.tlw.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
       NioEventLoopGroup group =  new NioEventLoopGroup();
        ChannelFuture channelFuture =  new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost",8080));

        Channel channel = channelFuture.sync().channel();
        log.debug("{}",channel);
        //启动一个新的线程来进行输入操作
        new Thread(()->{
            Scanner input = new Scanner(System.in);
            while(true){
                String line = input.nextLine();
                if("q".equals(line)){
                    channel.close();  //close异步操作 如1s后
                    //log.debug("处理关闭之后的操作");  //不能在这里善后
                    break;
                }
                channel.writeAndFlush(line);
            }
        },"input").start();

        //获取CloseFuture对象，1）同步处理关闭  2）异步处理关闭
        /*ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();
        log.debug("处理关闭之后的操作");*/

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.debug("处理关闭之后的操作");
                //关闭nio的线程
                group.shutdownGracefully();
            }
        });
    }
}
