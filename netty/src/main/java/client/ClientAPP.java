package client;


import client.handler.ClientInitializer;
import com.alibaba.fastjson.JSONObject;
import com.utopa.common.netty.client.model.Message;
import com.utopa.utopaar.api.dto.UtopaPianoUnityDTO;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.bootstrap.Bootstrap;

import java.net.InetSocketAddress;

public class ClientAPP {

    //ip 和 端口
    private final String host;
    private final int port;


    public ClientAPP(String host, int port){
        this.port = port;
        this.host = host;
    }

    /**
     * 链接服务端
     * @throws Exception
     */
    public void start() throws Exception{
        //region Description
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            //创建BootStrap
            Bootstrap b = new Bootstrap();
            //指定EvenLoopGroup 以处理客户端事件；需要适用于Nio的实现
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ClientInitializer());
            //异步连接并返回结果
            ChannelFuture f = b.connect().sync();
            if(f.channel() != null)  {
                System.out.println("连接服务端成功");
                //发送消息
//                Message message = initMessage();
//                f.channel().writeAndFlush(message);
            }
            //关闭链接
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }


    public static void main(String[] args) throws Exception{
        new ClientAPP("localhost", 31124).start();
    }



    /**
     * 封装消息对象
     * @param
     * @throws Exception
     */
    public Message initMessage() {
        Message message = new Message();
        message.setMsgId(11); //匹配服务的@Handler(MessageId.UTOPA_PIANO_PLAY)
        message.setFlag((byte)0);
        message.setType((byte)0);
        UtopaPianoUnityDTO content = new UtopaPianoUnityDTO();
        content.setCarrierId(4563L);
        byte[] contentBytes = JSONObject.toJSONBytes(content);
        message.setContent(contentBytes);
        message.setContentLength(contentBytes.length);
        return message;
    }


}
