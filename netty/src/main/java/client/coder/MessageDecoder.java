package client.coder;


import com.utopa.common.netty.client.model.Message;
import com.utopa.common.netty.client.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 解码器(反序列化)
 * 1）负责处理“入站 InboundHandler”数据，从一种格式到另一种格式
 * 2）将入站数据转换格式后传递到ChannelPipeline中的下一个ChannelInboundHandler进行处理
 * 3）ByteToMessageDecoder: 用于将字节转为消息，需要检查【缓冲区】是否有足够的字节
 *                         将接收到的二进制数据(Byte)解码，得到完整的请求报文(Message)。
 * 4）LengthFieldBasedFrameDecoder：长度编码解码器，将报文划分为报文头/报文体，
 *                         根据报文头中的Length字段确定报文体的长度，因此报文体的长度是可变的
 *
 * 参考：https://www.cnblogs.com/qdhxhz/p/10245936.html
 *
 * 协议格式：
 * 拆包
 * @author linjingfu
 *
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger log = LoggerFactory.getLogger(MessageDecoder.class);

    //构造方法
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    /***
     * 消息包
     */
    private Message message = null;
    /***
     * 读取缓存buffer
     */
    private byte[] readBuffer = new byte[0];
    /***
     * 协议头部信息长度
     */
    private static final int PROTOCOL_LEN = 11;
    /***
     * buffer
     */
    private ByteBuf tempBuf ;


    /**
     * 将ByteBuf数据解码成其他形式的数据。
     *
     *
     * 协议格式：
     *     数据校验头: 0xFF(固定值,1个字节)
     *     消息类型: MagId(int,4字节)
     *     数据类型: type(byte,1字节):0-json
     *     消息标志: flag(byte,1字节): 0-fail、1-suc
     *     数据长度：content.length+6(int,数据内容字节长度+6【消息类型+数据类型+消息标志】)
     *     数据内容：content(byte[]变长字节数组)
     *
     * @param ctx
     * @param byteBuf 需要解码的二进制数据
     * @return
     * @throws Exception
     */
    // 0xFF标志(1字节) +  消息类型msgId(4字节 ) + 类型type(1 字节 json/byte[]) +消息标志flag(1字节）+ 内容
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        //已经处理了TCP的粘包拆包问题，我们需要把字节流解码为业务对象Message
         if (this.message == null) {
            // 计算该数据包剩余需要读取的数据长度
            int needReadHeaderLen = PROTOCOL_LEN - readBuffer.length; //11[缓存readBuffer=0]
            if (needReadHeaderLen > byteBuf.readableBytes()) {  //false[数据byteBuf=20]
                needReadHeaderLen = byteBuf.readableBytes();
            }
            // 尝试读取数据
            byte[] bytes = new byte[needReadHeaderLen]; //11
            byteBuf.readBytes(bytes, 0, needReadHeaderLen); //byteBuf-》bytes(剩9)
            // 拷贝追加byte数据数组
            this.readBuffer = ByteUtils.bytesAppendArray(this.readBuffer, bytes); //bytes->readBuffer
            if (this.readBuffer.length < PROTOCOL_LEN) {
                System.out.println("decode: 数据头检验长度不足 " + this.readBuffer.length);
                return null;
            }
             this.tempBuf = Unpooled.copiedBuffer(this.readBuffer); //readBuffer->tempBuf
            //读取校验头
            int startFlag =   this.tempBuf.readUnsignedByte();//
            if (startFlag != 0xFF){
                System.out.println("decode: 数据校验头部消息错误，socket即将断开");
                ctx.channel().closeFuture().sync();
                return null;
            }
            //组装数据包
            Message tempPacket = new Message();
            //获取Message对象
            tempPacket.setContentLength(  this.tempBuf.readInt() - 6 ); //数据长度：content.length+6(int,数据内容字节长度+6【消息类型+数据类型+消息标志】)
            tempPacket.setMsgId(  this.tempBuf.readInt());
            tempPacket.setType(  this.tempBuf.readByte());
            tempPacket.setFlag(  this.tempBuf.readByte());
            //清空缓冲buffer
            this.readBuffer = new byte[0];
            this.message = tempPacket;
            this.tempBuf = null;
        }

        // 计算该数据包剩余需要读取的数据长度
        int needReadBodyLen = this.message.getContentLength() - readBuffer.length;
        if (needReadBodyLen > byteBuf.readableBytes()) {
            needReadBodyLen = byteBuf.readableBytes();
        }

        byte[] bytes = new byte[needReadBodyLen];
        byteBuf.readBytes(bytes, 0, bytes.length);

        // 拷贝追加byte数据数组
        this.readBuffer = ByteUtils.bytesAppendArray(this.readBuffer, bytes);
        Message message   = null;
        if (this.message.getContentLength()  == this.readBuffer.length) {
            // 解封装数据包
            this.message.setContent(this.readBuffer);
           // 还原缓冲数据
            this.readBuffer = new byte[0];
            message =   this.message ;
            this.message = null;
        }

        return  message;
    }

}
