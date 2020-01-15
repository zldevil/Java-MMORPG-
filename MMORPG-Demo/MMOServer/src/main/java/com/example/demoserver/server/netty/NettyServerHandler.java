package com.example.demoserver.server.netty;


import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.proto.MsgProto;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerQuitService;
import com.example.demoserver.server.net.dispatch.MessageDispatcher;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@ChannelHandler.Sharable
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    @Autowired
    private PlayerQuitService playerQuitService;
    //=(PlayerQuitService)SpringUtil.getBean("playerQuitService");

    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    @Autowired
    private BagService bagService;

    @Override
    //主要业务逻辑
    public void channelRead(ChannelHandlerContext ctx,Object msgObject) {


        MsgProto.Msg  msgProto =(MsgProto.Msg)msgObject;

        log.info("==========》 服务端收到  {}",msgObject);

        Msg msg =new Msg();

        msg.setId(msgProto.getMgsId());
        msg.setContent(msgProto.getContent());

        try {
            MessageDispatcher.messageReceived(ctx,msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public  void channelActive(@NotNull ChannelHandlerContext ctx ){


        log.info("客户端: " + ctx.channel().id() + " 加入连接");
        Notify.notifyByCtx(ctx,"欢迎来到阿拉德大陆");
        //Notify.notifyByCtx(ctx,"可以使用 show_cmd  展现所有指令");
        Notify.notifyByCtx(ctx,"测试账号登陆示例: login 1  123  ");

    }


    @Override
    //断开连接时执行此方法
    public void channelInactive(ChannelHandlerContext ctx){

        Player player=playerCacheMgr.getPlayerByCtx(ctx);

        ctx.writeAndFlush("正在断开连接");

        // 将角色信息保存到数据库
        playerQuitService.savePlayer(player);
       // bagService.saveBag(player);

        // 清除缓存
        playerQuitService.cleanPlayerCache(ctx);
        log.info("客户端: " + ctx.channel().id() + " 已经离线");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);


        log.error("服务器内部发生错误");
        Notify.notifyByCtx(ctx,"出现意外"+cause.getMessage());

        // 将角色信息保存到数据库
        playerQuitService.savePlayer(playerCacheMgr.getPlayerByCtx(ctx));

        log.error("发生错误 {}", cause.getMessage());

        // 打印错误
        cause.printStackTrace();
    }
}
