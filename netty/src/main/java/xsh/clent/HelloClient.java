package xsh.clent;

import client.ClientAPP;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty客户端
 */
public class HelloClient {

    private final String host;
    private final int port;

    //构造方法
    public HelloClient(String host, int port){
        this.port = port;
        this.host = host;
    }

    //启动方法
    private void start() throws InterruptedException {
        EventLoopGroup workgroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workgroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * @param sc
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pip = sc.pipeline();
                            //客户端处理逻辑
                            pip.addLast(new HelloClientHandler());
                        }
                    });

            //连接服务端，并异步返回结果
            ChannelFuture cf1 = b.connect(host, port).sync();
            Channel ch = cf1.channel();
            //向服务端发送消息
            ch.writeAndFlush(Unpooled.copiedBuffer("服务端，你接收到我的信息没?".getBytes()));
            //关闭连接
            ch.closeFuture().sync();
        }finally {
            workgroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        //1,使用自定义的netty客户端链接
//        new HelloClient("127.0.0.1", 8765).start();
        //2,使用netty-client.jar启动netty客户端，链接服务端(31124端口)
        new ClientAPP("localhost", 8085).start();
    }
}
