package com.example.demoserver.game.guild.service;

import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.game.guild.cache.GuildCache;
import com.example.demoserver.game.guild.model.Guild;
import com.example.demoserver.game.guild.model.PlayerJoinRequest;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.server.notify.Notify;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class GuildService {

    @Autowired
    private Notify notify;

    @Autowired
    private GuildCache guildCache;

    public void createGuild(Player player,String guildName){

        if(null!=player.getGuildId()){
            notify.notifyPlayer(player,"创建公会之前必须退出加入的公会");
        }

        Guild guild=new Guild(player.getId(),guildName,1);
        guild.getPlayerMap().put(player.getId(),player);
        player.setGuildId(player.getId());
        notify.notifyPlayer(player,"创建工会成功");

        //插入数据库
        //时间分发
        TODO 检测任务进度,例如第一次加入公户i;

    }

    public void joinGuild(Player player,String guildName){
        if(null!=player.getGuildId()){
            notify.notifyPlayer(player,"加入公会之前必须退出加入的公会");
        }

        Guild guild=guildCache.get(guildName);
        if(guild==null){
            notify.notifyPlayer(player,"此工会不存在");
        }

        guild.getPlayerMap().put(player.getId(),player);
        guild.setMember(guild.getMember()+1);

        guild.getPlayerJoinRequestMap().put(player.getId(),new PlayerJoinRequest(false,new Date(),player));
        //更新数据库
        notify.notifyPlayer(player,"加入公会请求已发送");

    }

    public void levelGuild(Player player,String guildName){

    }

    public void permitPlayerEnterGuild(Player player,Player targetPlayer,String guildName){
        if (player.getGuildId() == 0) {
            notify.notifyPlayer(player,"您并没有公会，不能操作");
            return;
        }
        if (player.getPositionInGuild()<2) {
            notify.notifyPlayer(player,"您的公会职位没有权限允许别人入会");
            return;
        }
        Guild guild = guildCache.getGuildById(player.getGuildId());
        PlayerJoinRequest playerJoinRequest = guild.getPlayerJoinRequestMap().get(targetPlayer.getId());
        if (null == playerJoinRequest) {
            notify.notifyPlayer(player,"该玩家并没有申请加入公会");
            return;
        }

        // 变更设置
        guild.getPlayerMap().put(targetPlayer.getId(),targetPlayer);
        //targetPlayer.setGuildClass(0);
        targetPlayer.setGuildId(guild.getId());
        playerJoinRequest.setAgree(true);
        notify.notifyPlayer(player, MessageFormat.format("已允许玩家{0}加入公会",targetPlayer.getName()));
        notify.notifyPlayer(targetPlayer,MessageFormat.format("你已加入{0}公会",guild.getName()));

        //guildManager.updateGuild(guild);
        //更新数据库

        // 公会事件,检测进度
        //EventManager.publish(new GuildEvent(joiner,guild));
    }
}
