package client.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * ChannelHandlerAdapter类实现了ChannelHandler接口(提供了许多事件处理的接口方法)，
 * 所以自定义的DiscardServerHandler只要覆盖这些方法即可。
 *
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * chanelRead()：事件处理方法，每当从客户端收到新的数据时，该方法会在收到消息时被调用
     * @param ctx：通道处理的上下文信息
     * @param msg：接收的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {

        try {
            //收到的消息的类型(ByteBuf)
            ByteBuf in = (ByteBuf) msg;
            //打印客户端输入，传输过来的的字符
            System.out.print(in.toString(CharsetUtil.UTF_8));
        } finally {
            /**
             * ByteBuf是一个引用计数对象，必须显示地调用release()释放。
             * 请记住处理器的职责是释放所有传递到处理器的引用计数对象。
             */
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }

    }

    /***
     * 这个方法会在发生异常时触发
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /**
         * exceptionCaught(): 事件处理方法,当出现Throwable对象才会被调用，即当 Netty 由于 IO
         * 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来 并且把关联的 channel
         * 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不 同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
         */
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }

}
