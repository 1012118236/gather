package com.example.gather.socket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPServerHandler  extends ChannelInboundHandlerAdapter  {

    private static final Logger log = LoggerFactory.getLogger(TCPServerHandler.class);

    public TCPServerHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object source) throws Exception {
        String str = new String((byte[]) source,"UTF-8");
        log.info("收到消息↓：");
        log.info(str);
        ctx.writeAndFlush(str.getBytes("UTF-8"));// 收到及发送，这里如果没有writeAndFlush，上面声明的ByteBuf需要ReferenceCountUtil.release主动释放
        ctx.close();

    }
}
