package client;

import ClientFrame.ClientFrame;
import com.sun.xml.internal.bind.v2.TODO;
import common.proto.MsgProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {


    public ClientHandler(){

    }

    //连接成功后执行的方法
    @Override
    public void channelActive(ChannelHandlerContext ctx){

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{

        //获取msg，显示在控制台上
        MsgProto.Msg message =(MsgProto.Msg)msg;
        System.out.println("客户端接收： "+message.getContent() + "\n");


        ClientFrame.InPutAndOutput.append(message.getContent() + "\n");
        // 使用JTextArea的setCaretPosition();手动设置光标的位置为最后一行。
        ClientFrame.InPutAndOutput.setCaretPosition( ClientFrame.InPutAndOutput.getDocument().getLength());

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //ctx.flush();
    }


    @Override

    /**
    * 发生错误关闭重连
     * */
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception {


        ctx.close();
        System.out.println("连接出现错误：");
        cause.printStackTrace();
        System.out.println("=====重新连接服务器  ===");
       //("================重新连接服务器==========");
        new DemoClient().connect(8888,"127.0.0.1");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        //重连服务器
        new DemoClient().connect(8888,"127.0.0.1");
    }
}
