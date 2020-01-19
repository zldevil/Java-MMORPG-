package com.example.demoserver.game.team.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.team.service.TeamService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Controller
@Component
public class TeamContoller {


    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private TeamService teamService;

    @Resource
    private Notify notify;


    @RequestMapping(getOrder = Orders.INVITEPLAYERJOINTEAM)
    public void invitePlayerJoinTeam(ChannelHandlerContext ctx, Msg message) {
        String[] parameter = SplitParameters.split(message);
        int inviteeId = Integer.valueOf(parameter[1]);
        Player invitee = playerDataService.getOnlinePlayerById(inviteeId);
        Player inviter =  playerDataService.getPlayerByCtx(ctx);
        teamService.invitePlayerJoinTeam(inviter,invitee);

    }


    @RequestMapping(getOrder = Orders.GETOUTPLAYERTEAM)
    public void getOutPlayerTeam(ChannelHandlerContext ctx, Msg message) {
        String[] parameter = SplitParameters.split(message);
        Integer targetPlayerId= Integer.valueOf(parameter[1]);
        Integer teamId=Integer.valueOf(parameter[2]);

        Player player=playerDataService.getPlayerByCtx(ctx);
        Player targetPlayer=playerDataService.getOnlinePlayerById(targetPlayerId);

        teamService.getOutPlayerTeam(player,targetPlayer,teamId);

    }


    @RequestMapping(getOrder = Orders.AGREEPLAYERJOINTEAM)
    public void agreeJoinPlayerTeam(ChannelHandlerContext channelHandlerContext,Msg message){
        String[] parameter = SplitParameters.split(message);

        Integer teamId = Integer.valueOf(parameter[1]);

        Player player =  playerDataService.getPlayerByCtx(channelHandlerContext);

        teamService.agreeJoinPlayerTeam(player,teamId);

    }

    @RequestMapping(getOrder = Orders.REFUSEPLAYERJOINTEAM)
    public void refusePlayerJoinTeam(ChannelHandlerContext ctx, Msg message){
        String[] parameter = SplitParameters.split(message);
        Integer targetPlayerId = Integer.valueOf(parameter[1]);
        Integer teamId=Integer.valueOf(parameter[2]);
        Player player =  playerDataService.getPlayerByCtx(ctx);
        Player targetPlayer=playerDataService.getOnlinePlayerById(targetPlayerId);
        teamService.refusePlayerJoinTeam(player,targetPlayer,teamId);

    }


    @RequestMapping(getOrder =Orders.JOINTEAM)
    public void joinTeam(ChannelHandlerContext ctx, Msg message) {
        String[] parameter = SplitParameters.split(message);
        int teamId = Integer.valueOf(parameter[1]);
        Player player =  playerDataService.getPlayerByCtx(ctx);

        teamService.joinTeam(player,teamId);
    }


    @RequestMapping(getOrder = Orders.REFUSEJOINTEAM)
    public void refuseJoinTeam(ChannelHandlerContext ctx, Msg message){
        String[] parameter = SplitParameters.split(message);
        Integer teamId = Integer.valueOf(parameter[1]);
        Player player =  playerDataService.getPlayerByCtx(ctx);

        teamService.refuseJoinTeam(player,teamId);

    }


    @RequestMapping(getOrder = Orders.EXITTEAM)
    public void exitTeam(ChannelHandlerContext ctx, Msg message)
    {
        String[] parameter = SplitParameters.split(message);
        int teamId = Integer.valueOf(parameter[1]);
        Player player =  playerDataService.getPlayerByCtx(ctx);
        teamService.exitTeam(player,teamId);
    }


    @RequestMapping(getOrder =Orders.SHOWTEAM )
    public void showTeam(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player =  playerDataService.getPlayerByCtx(channelHandlerContext);
        teamService.showTeam(player);
    }

}
