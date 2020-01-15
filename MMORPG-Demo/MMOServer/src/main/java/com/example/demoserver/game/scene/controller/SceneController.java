package com.example.demoserver.game.scene.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;

import com.example.demoserver.game.scene.manager.SceneCacheMgr;
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




@Slf4j
@Controller
@Component
public class SceneController  {




    @Autowired
    private GameSceneService gameSceneService;

    @Autowired
    private PlayerDataService playerDataService;




    @RequestMapping(getOrder = Orders.MOVE)
    public void playerMove(ChannelHandlerContext ctx, Msg message) {
        Player player = playerDataService.getPlayerByCtx(ctx);

        //参数
        String[] array = message.getContent().split("\\s+");
        int willMoveSceneId =  Integer.valueOf(array[1]);

        GameScene gameScene = SceneCacheMgr.getScene(willMoveSceneId);

        StringBuilder sb = new StringBuilder();

        if (gameSceneService.canMoveTo(player,gameScene)) {
            // 获取当前角色所在的场景
            gameSceneService.moveToScene(player,willMoveSceneId);
            sb.append(MessageFormat.format("你到达的地方是： {0} ", gameScene.display()));
        } else {

            sb.append(MessageFormat.format("这个地点不能到：{0}",gameScene.display()));

        }


          Notify.notifyByCtxWithMsgId(ctx,sb,message.getId());

    }
}

