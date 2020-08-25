package com.dean.proxy.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class NettyClientService extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端激活");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush("hhahah ok");
        System.out.println("over");
        //System.out.println(channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }

    /**
     * @ChannelHandler.Sharable,这个注解是为了线程安全，如果你不在乎是否线程安全，不加也可以。

    SimpleChannelInboundHandler，这里的类型可以是ByteBuf，也可以是String，还可以是对象，根据实际情况来。

    channelRead0，消息读取方法，注意名称中有个0。

    ChannelHandlerContext，通道上下文，代指Channel。

    ByteBuf,字节序列，通过ByteBuf操作基础的字节数组和缓冲区，因为JDK原生操作字节麻烦、效率低，所以Netty对字节的操作进行了封装，实现了指数级的性能提升，同时使用更加便利。

    CharsetUtil.UTF_8，这个是JDK原生的方法，用于指定字节数组转换为字符串时的编码格式。


     */
}
