package com.example.demoserver.game.chat.service;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.Map;

@Service
public class ChatService {

    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private PlayerCacheMgr playerCacheMgr;

    @Autowired
    private Notify notify;



    //私聊在线玩家
    public void privateChat(Player player ,Integer targetPlayerId ,String word){

        String message = MessageFormat.format("{0}悄悄对你说： {1}",
                player.getName(),word);
        Player targetPlayer = playerDataService.getOnlinePlayerById(targetPlayerId);
        if (null == targetPlayer ){
            notify.notifyPlayer(player,"玩家不在线");
        }
        notify.<String>notifyPlayer(targetPlayer,message);

        notify.notifyPlayer(player,"消息发送成功");
    }


    public void publicChat(Player player,String word){

       Map<ChannelHandlerContext,Player> map= playerCacheMgr.getAllPlayerCache();
       map.values().forEach(playerTmp ->{
           notify.notifyPlayer(playerTmp,MessageFormat.format("玩家{0}收到了{1}",playerTmp.getName(),word));
       } );
    }

}
