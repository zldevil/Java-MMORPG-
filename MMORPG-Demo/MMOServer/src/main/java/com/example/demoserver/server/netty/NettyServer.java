package com.example.demoserver.server.netty;


import com.example.demoserver.common.proto.MsgProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class NettyServer {

    public  void bind(int port) throws  Exception {
        EventLoopGroup  bossGroup =new NioEventLoopGroup();
        EventLoopGroup  workerGroup =new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap =new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel){
                            socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());

                            socketChannel.pipeline().addLast(new ProtobufDecoder(MsgProto
                                    .Msg.getDefaultInstance()));
                            socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            socketChannel.pipeline().addLast(new ProtobufEncoder());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
        ChannelFuture f =bootstrap.bind(port).sync();

        System.out.println("绑定端口成功");
        //等待服务器监听端口关闭
        f.channel().closeFuture().sync();

    }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @PostConstruct
    public void start(){
        int port =9999;
        try {
            new NettyServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败了");
        }
    }


}

