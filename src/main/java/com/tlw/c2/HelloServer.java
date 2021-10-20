package com.tlw.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.BoosEventLoop,WorkEventLoop(selector,thread),
                .group(new NioEventLoopGroup())
                //3.选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4.boss负责处理连接，work(child) 负责处理读写，决定worker(child)能执行那些操作(handler)
                .childHandler(
                        //5.channel代表和客户端进行数据读写的通道，initializer初始化，负责加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            //7.连接建立后后被调用
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder());  //ByteBuf->字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override  //读事件
                                    public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{
                                        //打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //6.绑定监听端口
                .bind(8080);
    }
}
