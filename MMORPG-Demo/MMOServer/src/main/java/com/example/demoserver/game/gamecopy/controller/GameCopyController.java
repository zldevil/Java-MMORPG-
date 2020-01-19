package com.example.demoserver.game.gamecopy.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.gamecopy.service.GameCopyService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.team.cache.TeamCache;
import com.example.demoserver.game.team.model.Team;
import com.example.demoserver.game.team.service.TeamService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Controller
@Component
public class GameCopyController {

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private GameCopyService gameCopyService;

    @Autowired
    private TeamCache teamCache;

    @Autowired
    private Notify notify;

    @RequestMapping(getOrder = Orders.SHOWGAMECOPY)
    public void showGameCopy(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        gameCopyService.showGameCopy(player);
    }


    @RequestMapping(getOrder =Orders.ENTERGAMECOPY )
    public void enterGameCopy(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args= SplitParameters.split(message);
        Integer gameCopyId=Integer.valueOf(args[1]);
        gameCopyService.enterGameCopy(player,gameCopyId);
    }

    @RequestMapping(getOrder = Orders.ENTERTEAMGAMECOPY)
    public void enterTeamGameCopy(ChannelHandlerContext channelHandlerContext,Msg message){
        String[] args= SplitParameters.split(message);
        Integer copyId=Integer.valueOf(args[0]);
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        Team team=teamCache.getCacheTeam(player.getTeamId());
        if (Objects.isNull(team)) {
            notify.notifyPlayer(player,"你没有队伍");
            return;
        }
        if (!player.getId().equals(team.getCaptainId())) {
            notify.notifyPlayer(player,"你不是队长，无法开始副本");
            return;
        }

        gameCopyService.enterTeamGameCopy(team.getTeamPlayer().values(),copyId);
    }


    @RequestMapping(getOrder = Orders.EXITGAMECOPY)
    public void exitGameCopy(ChannelHandlerContext channelHandlerContext,Msg message){

    }

}
