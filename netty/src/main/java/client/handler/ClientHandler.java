package client.handler;

import com.alibaba.fastjson.JSONObject;
import com.utopa.common.netty.client.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * 启动时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //发送数据给服务器
        new Thread(() -> {
            while (true) {
                System.out.println("请输入:") ;
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                if("quit".equals(line)) {
                    ctx.close();
                    System.out.println("客户端运行完毕");
                } else {
                    Message message = new Message();
                    message.setMsgId(11); //匹配服务的@Handler(MessageId.UTOPA_PIANO_PLAY)
                    message.setFlag((byte)1);
                    message.setType((byte)0);
                    //消息体
                    Map<String, Object> map = new HashMap<>();
                    map.put("carrierId", 4563L);
                    byte[] contentBytes = JSONObject.toJSONString(map).getBytes();
                    message.setContent(contentBytes);
                    message.setContentLength(contentBytes.length);

                    try {
                        //发送给服务端
                        ctx.writeAndFlush(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理接收的信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        //消息编码
        String string  = new String(msg.getContent(),"utf-8");
        System.out.println("客户端接收消息:" + string);
    }

}
