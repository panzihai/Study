package client.coder;

import com.utopa.common.netty.client.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 编码器(序列化)
 *  1）负责“出站 OutboundHandler” 数据
 *  2）将数据转换成协议规定的二进制格式发送
 *  3）编码的结果是将信息转换成二进制流放入ByteBuf中
 *  Message：需要编码的对象
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    /**
     * 输出对象out是一个ByteBuf实例，我们应该将泛型参数msg包含的信息写入到这个out对象中。
     *
     *
     * 协议格式：
     *     数据校验头: 0xFF(固定值,1个字节)
     *     消息类型: MagId(int,4字节)
     *     数据类型: type(byte,1字节):0-json
     *     消息标志: flag(byte,1字节): 0-fail、1-suc
     *     数据长度：content.length+6(int,数据内容字节长度+6【消息类型+数据类型+消息标志】)
     *     数据内容：content(byte[]变长字节数组)
     * */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 0xFF标志(1字节) +  消息类型MsgId(4字节) + 数据类型type(1字节 json/byte[]) +消息标志flag(1字节) +内容
        int length = msg.getContent().length + 6  ;
        //header
        out.writeByte(0xFF);
        out.writeInt(length);//将Integer转成【二进制】字节流写入ByteBuf中
        out.writeInt(msg.getMsgId());
        out.writeByte(msg.getType());
        out.writeByte(msg.getFlag());
        //content
        out.writeBytes(msg.getContent());

        ctx.flush();
    }

}
