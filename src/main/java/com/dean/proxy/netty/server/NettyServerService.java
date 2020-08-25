package com.dean.proxy.netty.server;

import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.Hutool;
import cn.hutool.crypto.SecureUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class NettyServerService extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //处理收到的数据，并反馈消息到到客户端
        ByteBuf in = (ByteBuf)msg;
        System.out.println("收到客户端发过来的消息: " + in.toString(CharsetUtil.UTF_8));
        Channel channel = ctx.channel();

        //写入并发送信息到远端（客户端）
        ChannelFuture cf = channel.writeAndFlush(Unpooled.copiedBuffer("你好，我是服务端，我已经收到你发送的消息", CharsetUtil.UTF_8));

        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //写操作完成，并没有错误发生
                if (future.isSuccess()){
                    System.out.println("successful");
                }else{
                    //记录错误
                    System.out.println("error");
                    future.cause().printStackTrace();
                }
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server complete");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);


        //super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //出现异常的时候执行的动作（打印并关闭通道）
        cause.printStackTrace();
        ctx.close();
    }

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        int a  = atomicInteger.addAndGet(2);
        System.out.println(a);

        boolean b = atomicInteger.compareAndSet(3, 5);
        System.out.println(atomicInteger.get());

        int i = atomicInteger.incrementAndGet();
        System.out.println(i);
        String s = SecureUtil.md5("1");
        System.out.println(s);
    }

}
