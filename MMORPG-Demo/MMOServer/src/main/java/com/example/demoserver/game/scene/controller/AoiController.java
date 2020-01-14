package com.example.demoserver.game.scene.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scene.model.GameScene;
import com.example.demoserver.game.scene.servcie.GameSceneService;
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
public class AoiController {

    @Autowired
    private PlayerDataService  playerDataService;

    @Autowired
    private GameSceneService   gameSceneService;


    /**  获取当前场景全部实体
     * @param ctx   上下文
     * @param message 信息
     */
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
            Notify.notifyByCtx(ctx,MessageFormat.format("怪物的id: {0},怪物名字为: {1}",monster.getId(),monster.getName()));
        });

        gameScene.getNpcs().values().stream().forEach(npc -> {
            Notify.notifyByCtx(ctx,MessageFormat.format("NPC的id: {0},NPC名字为: {1}",npc.getId(),npc.getName()));
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
