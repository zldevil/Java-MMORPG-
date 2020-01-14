package com.example.demoserver.game.SceneEntity.controller;


import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;


import com.example.demoserver.game.SceneEntity.service.GameObjectService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;

import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Controller
@Component
public class SceneObjectController {

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private GameObjectService gameObjectService;


    @RequestMapping(getOrder = Orders.TALKWITHNPC)
    public void talkWithNPC(ChannelHandlerContext ctx, Msg message) {
        String[] args = SplitParameters.split(message);
        Player player = playerDataService.getPlayer(ctx);
        Integer npcId = Integer.valueOf(args[1]);
        gameObjectService.talkWithNpc(npcId,ctx);

    }

}
