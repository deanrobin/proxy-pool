package com.dean.proxy.netty.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class AppNettyClient {

    private final String host;
    private final int port;

    public AppNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        /**
         * @Author 江成军
         * @Date 2020/6/1 11:28
         * @Description 配置相应的参数，提供连接到远端的方法
         **/
        EventLoopGroup group = new NioEventLoopGroup();//I/O线程池
        try {
            Bootstrap bs = new Bootstrap();//客户端辅助启动类
            bs.group(group)
                .channel(NioSocketChannel.class)//实例化一个Channel
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>()//进行通道初始化配置
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientService());//添加我们自定义的Handler
                    }
                });

            //连接到远程节点；等待连接完成
            ChannelFuture future = bs.connect().sync();

            Channel channel = future.channel();

            //发送消息到服务器端，编码格式是utf-8
            ChannelFuture hello_world = channel.writeAndFlush(
                Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER);

            hello_world.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    //写操作完成，并没有错误发生
                    if (future.isSuccess()){
                        System.out.println("successful");
                        //future.channel().writeAndFlush(Unpooled.copiedBuffer("SB", CharsetUtil.UTF_8));

                    }else{
                        //记录错误
                        System.out.println("error");
                        future.cause().printStackTrace();
                    }
                }
            });


            //阻塞操作，closeFuture()开启了一个channel的监听器（这期间channel在进行各项工作），直到链路断开
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AppNettyClient("127.0.0.1", 18080).run();
    }

    /**
     * bs.connect().sync()，这里的sync()表示采用的同步方法，这样连接建立成功后，才继续往下执行。

     pipeline()，连接建立后，都会自动创建一个管道pipeline，这个管道也被称为责任链，保证顺序执行，同时又可以灵活的配置各类Handler
     ，这是一个很精妙的设计，既减少了线程切换带来的资源开销、避免好多麻烦事，同时性能又得到了极大增强。
     */

}
