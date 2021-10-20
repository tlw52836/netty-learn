package com.tlw.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        //细分2：可以创建一个独立的EventLoopGroup用于处理耗时比较长的channel中的handler处理，以免其他channel等待
        EventLoopGroup group = new DefaultEventLoopGroup(2);
        new ServerBootstrap()
                //细分1：
                //boss只负责ServerSocketChannel上accept事件，只需要一个线程（默认也是1）
                //worker只负责socketChannel上的读写
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override  //此处msg:ByteBuf类型
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));
                                ctx.fireChannelRead(msg);  //将消息传递给下一个handler
                            }
                        }).addLast(group,"handler1",new ChannelInboundHandlerAdapter(){  //交给group进行处理，不通过worker
                            @Override  //此处msg:ByteBuf类型
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
