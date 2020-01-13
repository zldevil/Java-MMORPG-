package com.example.demoserver.game.guild.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.guild.service.GuildService;
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
public class GuildController {

    @Autowired
    private GuildService guildService;

    @Autowired
    private PlayerDataService playerDataService;


    @RequestMapping(getOrder = Orders.CREATEGUILD)
    public void createGuild(ChannelHandlerContext channelHandlerContext, Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args=SplitParameters.split(message);
        String nameGuild=args[1];
        guildService.createGuild(player,nameGuild);

    }


    @RequestMapping(getOrder = Orders.LEAVEGUILD)
    public void leaveGuild(ChannelHandlerContext channelHandlerContext,Msg message){

        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args=SplitParameters.split(message);
        String nameGuild=args[1];
        guildService.levelGuild(player,nameGuild);
    }


    @RequestMapping(getOrder = Orders.JIONGUILD)
    public void joinGuild(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args=SplitParameters.split(message);
        String nameGuild=args[1];
        guildService.joinGuild(player,nameGuild);
    }


    public void permitPlayerEnterGuild(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args=SplitParameters.split(message);
        Player targetPlayer = playerDataService.getPlayerById(Integer.valueOf(args[1]));
        String guildName=String.valueOf(args[2]);
        guildService.permitPlayerEnterGuild(player,targetPlayer,guildName);

    }

}
