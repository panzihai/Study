package xsh.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

/**
 * 服务端的逻辑handler
 *
 * writeAndFlush(): 将信息写入Buffer并刷入
 */
public class HelloServerHandler  extends SimpleChannelInboundHandler<Object> {

    /**
     * 处理收到的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //收到的消息
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        //打印收到的消息
        System.out.println("客户端【"+ctx.channel().remoteAddress()+"】说: " + request);
        //回复客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("收到！".getBytes()));
        //.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在channel被启用的时候触发 (在建立连接的时候)
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端[" + ctx.channel().remoteAddress() + "]已连上!");
        //回复客户端
        ctx.writeAndFlush( "欢迎连接【" + InetAddress.getLocalHost().getHostName() + "】服务端!");

        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
