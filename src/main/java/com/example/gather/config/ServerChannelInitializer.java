package com.example.gather.config;

import com.example.gather.socket.MessagePacketDecoder;
import com.example.gather.socket.MessagePacketEncoder;
import com.example.gather.socket.TCPServerHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    public ServerChannelInitializer() throws InterruptedException {
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(15, 0, 0, TimeUnit.MINUTES));
        // netty基于分割符的自带解码器，根据提供的分隔符解析报文，这里是0x7e;1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
//        pipeline.addLast(
//                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[] { 0x7e }),
//                        Unpooled.copiedBuffer(new byte[] { 0x7e })));
        //自定义编解码器
        pipeline.addLast(
                new MessagePacketDecoder(),
                new MessagePacketEncoder()
        );
        //自定义Hadler
        pipeline.addLast("handler",new TCPServerHandler());
        //自定义Hander,可用于处理耗时操作，不阻塞IO处理线程
//        pipeline.addLast(group,"BussinessHandler",new BussinessHandler());
    }
}
