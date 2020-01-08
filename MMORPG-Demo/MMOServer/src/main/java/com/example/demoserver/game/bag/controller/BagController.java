package com.example.demoserver.game.bag.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.service.BagService;

import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class BagController  {


    @Autowired
    private BagService bagService;

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private Notify notificationManager;


    
    @RequestMapping(getOrder = Orders.PACKBAG)

    public void packBag(ChannelHandlerContext ctx, Msg message) {
        bagService.packBag(ctx);
    }


    @RequestMapping(getOrder =Orders.SHOWBAG)
    public void showBag(ChannelHandlerContext ctx, Msg message) {
        Player player = playerDataService.getPlayerByCtx(ctx);
        if (null==player) {
            Notify.notifyByCtx(ctx,"角色没登陆");
            return;
        }

        Map<Integer, Item> itemMap = bagService.show(player);
        log.debug("itemMap {}",itemMap);

        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("背包：{0} 大小: {1}",
                player.getBag().getBagName(),player.getBag().getBagSize())).append("\n");

        if (0 == itemMap.size() ) {
            sb.append("背包空荡荡的");
        }

        StringBuilder itemString=new StringBuilder();
        itemMap.values().forEach(item -> {
            itemString.append(MessageFormat.format("物品:{0}  数量:{1}",item.getItemInfo().getName(),item.getCount()
            ));
        });
        Notify.notifyByCtxWithMsgId(ctx,sb.toString(),message.getId());
    }
}
