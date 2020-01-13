package com.example.demoserver.game.bag.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Controller
@Component
public class ItemController {


    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private Notify notify;

    @Autowired
    private BagService bagService;


    @RequestMapping(getOrder = Orders.USEITEM)
    private void useItem(ChannelHandlerContext ctx, Msg message) {

        String[] command = SplitParameters.split(message);

        Integer itemId = Integer.valueOf(command[1]);

        Integer count=Integer.valueOf(command[2]);

        Player player = playerDataService.getPlayer(ctx);

        boolean flag  = bagService.useItem(player,itemId,count);

        String result ;
        if (flag) {
            result = "使用物品成功";
        } else {
            result = "使用物品失败,角色未拥有这个物品或其他原因";
        }

        notify.notifyPlayer(player,result);
    }
}
