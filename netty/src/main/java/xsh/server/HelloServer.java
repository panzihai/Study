package xsh.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 参考：https://blog.csdn.net/aitang3496/article/details/102341153
 *
 * netty服务端
 *
 */
public class HelloServer {

    //服务端口
    private final int port = 8765;

    public static void main(String[] args) throws Exception {
        new HelloServer().run();
    }

    //启动方法
    private void run() throws InterruptedException {
        //1 第一个线程组 用于接收Client端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 第二个线程组 用于实际的业务处理操作的
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //3 创建一个辅助类Bootstrap，对我们的Server进行一系列的配置
            ServerBootstrap b = new ServerBootstrap();
            //把俩个工作线程组加入进来
            b.group(bossGroup, workerGroup)
                    //指定使用NioServerSocketChannel通道
                    .channel(NioServerSocketChannel.class)
                    //使用childHandler绑定 事件处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pip = sc.pipeline();
                            //自定义的逻辑handler
                            pip.addLast(new HelloServerHandler());
                        }
                    });

            //绑定端口 进行监听
            ChannelFuture f = b.bind(port).sync();
            System.out.println("启动服务【"+port+"】成功!") ;

            //Thread.sleep(1000000);
            //关闭监听
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
