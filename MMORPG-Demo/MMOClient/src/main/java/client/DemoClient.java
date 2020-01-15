package client;

import ClientFrame.ClientFrame;
import common.Orders;
import org.apache.commons.lang3.StringUtils;
import common.proto.MsgProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DemoClient {

    static {
         ClientFrame clientFrame =new ClientFrame();
    }

    public static Channel channel = null;

    public void connect(int port,String host) throws Exception{
        EventLoopGroup group =new NioEventLoopGroup();
        try{
            Bootstrap bootstrap =new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    //不需要等待数量直接发送
                        .option(ChannelOption.TCP_NODELAY,true)
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            public void initChannel(SocketChannel socketChannel){
                                socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                                socketChannel.pipeline().addLast(new ProtobufDecoder(MsgProto.Msg.getDefaultInstance()));
                                socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                                socketChannel.pipeline().addLast(new ProtobufEncoder());
                                socketChannel.pipeline().addLast(new ClientHandler());

                                channel = socketChannel;
                            }
                        });

            ChannelFuture f =bootstrap.connect(host, port).sync();

            // loop();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

   /* private void loop() throws IOException {
        ClientFrame.InPutAndOutput.append("========== 连接服务器成功==========\n");
        while (true) {
            System.out.println("请输入您的操作：  操作 + 数据（多个数据之间用空格隔开） (或者请使用指令` show_cmd `查看指令说明)");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String content = reader.readLine();
            System.out.println("客户端输入： "+content);
            if (StringUtils.isNotEmpty(content)) {
                if (StringUtils.equalsIgnoreCase(content, "q")) {
                    System.exit(1);
                }
                String[] array = content.split("\\s+");
                Orders orders = Orders.findByOrder(array[0]);
                MsgProto.Msg msg =MsgProto.Msg.newBuilder()
                        .setMgsId(orders.getOrderId())
                        .setContent(content).build();

                //向服务端发送内容
                channel.writeAndFlush(msg);
            }

        }
    }*/


    public  static void main(String[] args){
        int port=9999;
        if(args!=null&&args.length>0){
            try{
                port=Integer.valueOf(args[0]);

            }catch (NumberFormatException e){

            }
        }
        try {
            new DemoClient().connect(port,"127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
