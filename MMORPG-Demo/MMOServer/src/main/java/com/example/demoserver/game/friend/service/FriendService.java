package com.example.demoserver.game.friend.service;

import com.alibaba.fastjson.JSON;
import com.example.demoserver.game.friend.model.Friend;
import com.example.demoserver.game.player.manager.RoleCache;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.notify.Notify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private Notify notify;

    @Autowired
    private RoleCache roleCache;


    public void addFriend(Player player,Integer targetPlayerId){

        Player targetPlayer =playerDataService.getPlayerById(targetPlayerId);
        Optional.ofNullable(targetPlayer).map(
                playerTmp -> {
                    Friend friend = new Friend(
                            playerTmp .getId(),
                            playerTmp .getName(),
                            RoleCache.getRole(playerTmp .getTypeId()).getRoleName(),
                            new Date()
                    );
                    player.getFriendMap()
                            .put(friend.getPlayerId(),friend);
                    player.setFriends(JSON.toJSONString(player.getFriendMap()));
                    notify.notifyPlayer(player, MessageFormat.format("添加好友{0}成功",playerTmp .getName()));
                    return friend;
                }
        ).orElseGet(() ->  { notify.notifyPlayer(player,"添加的玩家不存在"); return null;});

    }

    public void listFriend(Player player){
        StringBuilder sb = new StringBuilder();
        sb.append("好友列表：\n");
        player.getFriendMap().values()
                .forEach(
                        friend -> {
                            friend.setOnline(playerDataService.getOnlinePlayerById(friend.getPlayerId()) == null);
                            //friend.setLastOnlineTime(player.getLastOnlineTime());
                            sb.append(MessageFormat.format("id:{0}  名字：{1} 职业：{2} 是否在线：{3}  最近的上线时间{4} \n",
                                    friend.getPlayerId(),friend.getName(),friend.getRoleClass(),
                                    friend.isOnline(),friend.getLastOnlineTime()));
                        }
                );
        if (player.getFriendMap().isEmpty()){
            sb.append("好友列表为空\n");
        }
        notify.notifyPlayer(player,sb.toString());
    }
}
