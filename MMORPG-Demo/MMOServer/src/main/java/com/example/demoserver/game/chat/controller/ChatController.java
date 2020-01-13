package com.example.demoserver.game.chat.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.chat.service.ChatService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Parameter;

@Controller
@Component
public class ChatController {

    @Resource
    private ChatService chatService;

    @Resource
    private PlayerDataService playerDataService;


    @RequestMapping(getOrder = Orders.PRIVATECHAT)
    public void privateChat(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerDataService.getPlayer(channelHandlerContext);
        String[] args= SplitParameters.split(message);
        Integer targetPleyerId =Integer.valueOf(args[1]);
        String  word =args[2];
        chatService.privateChat(player,targetPleyerId,word);
    }

    @RequestMapping(getOrder = Orders.PUBLICCHAT)
    public void publicChat(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerDataService.getPlayer(channelHandlerContext);
        String[] args= SplitParameters.split(message);
        String word=args[1];
        chatService.publicChat(player,word);

    }
}
