package com.example.demoserver.game.shop.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.shop.service.ShopService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Controller
@Component
public class ShopController {


    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private ShopService shopService;

    @RequestMapping(getOrder = Orders.BUGGOOD)
    public void buyGoods(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerDataService.getPlayer(channelHandlerContext);
        String[] args = SplitParameters.split(message);
        Integer goodId =Integer.valueOf(args[1]);
        Integer num=Integer.valueOf(args[2]);

        shopService.buyGood(player,1,goodId,num);

    }


    @RequestMapping(getOrder = Orders.SHOWGOODSONSHOP)
    public void showGoodsOnShop(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayer(channelHandlerContext);
        String[] args = SplitParameters.split(message);
        Integer shopId =Integer.valueOf(args[1]);
        shopService.showGoodsOnShop(player,shopId);
    }


}
