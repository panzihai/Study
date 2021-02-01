package client.handler;

import client.coder.MessageEncoder;
import com.utopa.common.netty.client.coder.MessageDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    //编解码器
    private static final MessageEncoder ENCODER = new MessageEncoder();

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // 添加编解码器, 由于ByteToMessageDecoder的子类无法使用@Sharable注解,
        pipeline.addLast(ENCODER);//编码器
        pipeline.addLast(new MessageDecoder(1024 * 10, 1, 4));//解码器

        //添加自定义的handler逻辑
        pipeline.addLast(new ClientHandler());

    }
}
