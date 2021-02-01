package xsh.clent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 使用SimpleChannelInboundHandler作为Handler处理事务
 */
public class HelloClientHandler extends SimpleChannelInboundHandler<Object> {

    public HelloClientHandler() {
        super();
    }

    /**
     * 处理接收的信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //接收的信息
            ByteBuf buf = (ByteBuf)msg;
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            String request = new String(data, "utf-8");
            //打印信息
            System.out.println("服务端【"+ctx.channel().remoteAddress()+"】说: " + request);
        } finally {
            /**
             * 参考：https://www.mingdeju.com/archives/52.html
             * 1)SimpleChannelInboundHandler会自动释放内存（虽然这是一种软释放）即是refCnt引用数减一。
             * 2)若再次release手动释放，会报错refCnt: 0, decrement: 1
             */
//            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 启动时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端启动！");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端关闭！");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端出现异常了！");
        cause.printStackTrace();
        ctx.close();
    }
}