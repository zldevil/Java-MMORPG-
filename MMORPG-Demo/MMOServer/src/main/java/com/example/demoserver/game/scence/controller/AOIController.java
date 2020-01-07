package com.example.demoserver.game.scence.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;

import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.text.MessageFormat;

import java.util.*;


@Slf4j
@Controller
@Component
public class AOIController  {


    @Autowired
    private PlayerDataService  playerDataService;

    @Autowired
    private GameSceneService   gameSceneService;

    /**
     * @param ctx   上下文
     * @param message 信息
     */

    //获取当前场景全部实体
    @RequestMapping(getOrder = Orders.AOI)
    public void aoi(ChannelHandlerContext ctx, Msg message) {

        Player player = playerDataService.getPlayer(ctx);

        GameScene gameScene = gameSceneService.getSceneByPlayer(player);



        //StringBuilder sbOfPlayer =new StringBuilder();
        gameScene.getPlayers().values().stream().forEach(player1 -> {
            StringBuilder sb =new StringBuilder();
            // sbOfPlayer.append(player1.getName());
            sb.append(player1.getName());
            sb.append(player1.getId());
            //注释一下
            Notify.notifyByCtx(ctx,sb);
        });

            //Notify.notifyByCtx(ctx,sbOfPlayer);


        gameScene.getMonsters().values().stream().forEach(monster -> {
            StringBuilder sb =new StringBuilder();
            sb.append(monster.getId());
            sb.append(monster.getName());
            Notify.notifyByCtx(ctx,sb);
        });

        gameScene.getNpcs().values().stream().forEach(npc -> {
            StringBuilder sb =new StringBuilder();
            sb.append(npc.getName());
            sb.append(npc.getId());
            //注释一下
            Notify.notifyByCtx(ctx,sb);
        });


    }


    @RequestMapping(getOrder = Orders.LOCATION)
    public void location(ChannelHandlerContext ctx, Msg message) {

        Player player = playerDataService.getPlayer(ctx);

        GameScene gameScene = gameSceneService.getSceneByPlayer(player);

        List<GameScene> gameSceneList = gameSceneService.getNeighborsSceneByIds(gameScene.getNeighborScence());

        String location = MessageFormat.format("当前场景是： {0} \n",gameScene.getName() );
        StringBuilder sb = new StringBuilder();
        sb.append(location);
        sb.append("相邻的地图是： ");
        gameSceneList.forEach(
                neighbor -> sb.append(MessageFormat.format("{0}: {1} ",neighbor.getId(), neighbor.getName() ))
        );

        Notify.notifyByCtxWithMsgId(ctx,sb,message.getId());
    }


}
