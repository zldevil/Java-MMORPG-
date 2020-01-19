package com.example.demoserver.game.team.service;

import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.game.gamecopy.model.GameCopy;
import com.example.demoserver.game.gamecopy.service.GameCopyService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scene.servcie.GameSceneService;
import com.example.demoserver.game.team.cache.TeamCache;
import com.example.demoserver.game.team.model.Team;
import com.example.demoserver.server.net.utils.IDUtil;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TeamService {

    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private Notify notify;

    @Autowired
    private TeamCache teamCache;


    /**
     * 邀请玩家加入自己的队伍
     *
     * @param invitee 被邀请者
     * @return 是否邀请成功
     */
    public void invitePlayerJoinTeam(Player invitor, Player invitee) {

        // 如果被要求者已经有队伍了，不能组队
        if (!(-1 == invitee.getTeamId())) {
            notify.notifyPlayer(invitor, "该玩家已经在别的队伍中");
            return;
        }
        Team team;
        if (teamCache.getCacheTeam(invitor.getTeamId()) == null) {
            team = new Team();
            team.getTeamPlayer().put(invitor.getId(), invitor);
            team.setCaptainId(invitor.getId());
            team.setId(IDUtil.getid());
            teamCache.put(team.getId(), team);
        }
        team = teamCache.getCacheTeam(invitor.getTeamId());

        notify.notifyPlayer(invitor, MessageFormat.format("已向玩家 {0} 发起组队请求", invitee.getName()));
        notify.notifyPlayer(invitee, MessageFormat.format(
                "玩家 {0} 向你发起发起组队请求,队伍id为:{1},如果同意请输入 agreejoinplayerteam teamId",
                invitor.getName(),team.getId()));

    }


    public void agreeJoinPlayerTeam(Player player, Integer teamId) {

        Team team=teamCache.getCacheTeam(teamId);

        if(null==team){
            notify.notifyPlayer(player,"不存在此队伍");
        }

        if(team.getTeamPlayer().size()<=team.getTeamSize()){
            team.getTeamPlayer().put(player.getId(),player);
            player.setTeamId(teamId);
            team.getTeamPlayer().values().forEach(playerTmp -> {
                notify.notifyPlayer(playerTmp, MessageFormat.format("玩家{0}已加入队伍，队伍id为:{1}",
                        player.getName(),team.getId()));

            });
        }else {
            notify.notifyPlayer(player,"该队伍人数已达到上限，无法加入队伍");
        }

    }


    public void getOutPlayerTeam(Player player ,Player targetPlayer,Integer teamId){

        Team team=teamCache.getCacheTeam(teamId);
        if(null==team){
            notify.notifyPlayer(player,"没有此队伍");
        }

        if(!targetPlayer.getTeamId().equals(teamId)){
            notify.notifyPlayer(player,"该玩家不在此队伍中，请检查队伍id或玩家id");
        }

        if(player.getId().equals(team.getCaptainId())){

            team.getTeamPlayer().remove(targetPlayer.getId());
            targetPlayer.setTeamId(-1);
            notify.notifyPlayer(player,"踢出玩家成功");

        }else {
            notify.notifyPlayer(player,"您不是队长，没有权限踢出队友");
        }

    }


    public void refusePlayerJoinTeam(Player player,Player targetPlayer,Integer teamId){

        Team team=teamCache.getCacheTeam(teamId);
        if(team.getCaptainId().equals(player.getId())){
            notify.notifyPlayer(targetPlayer,MessageFormat.format("队长{0}拒绝了你的加入队伍的请求",player.getName()));
        }else {
            notify.notifyPlayer(player,"您不是队长，无法执行该操作");
        }

    }


    public void joinTeam(Player player,Integer teamId) {

        Team team=teamCache.getCacheTeam(teamId);
        if(null==team){
            notify.notifyPlayer(player,"没有此队伍");
        }

        if(team.getTeamSize()>team.getTeamPlayer().size()){

            Player captainPlayer=playerDataService.getOnlinePlayerById(team.getCaptainId());

            notify.notifyPlayer(captainPlayer,MessageFormat.format("玩家{0},id为{1}请求加入队伍,队伍id为{2}",
                    player.getName(),player.getId(),teamId));
        }

        // 组队事件
        //EventManager.publish(new TeamEvent(Arrays.asList(invitee,invitor)));
    }


    public void refuseJoinTeam(Player player,Integer teamId){
        Team team=teamCache.getCacheTeam(teamId);
        Player captain=playerDataService.getOnlinePlayerById(team.getCaptainId());
        notify.notifyPlayer(captain,MessageFormat.format("玩家{0}拒接加入队伍",player.getName()));
    }


    public void exitTeam(Player player,Integer teamId){
        Team team =teamCache.getCacheTeam(teamId);
        if(player.getId().equals(team.getCaptainId())){
            if(team.getTeamPlayer().size()>1){
                team.getTeamPlayer().remove(player.getId());
                Player[] arrayplayer=(Player[]) team.getTeamPlayer().values().stream().toArray();
                team.setCaptainId(arrayplayer[0].getId());

            }else {
                team.getTeamPlayer().remove(player.getId());
            }
        }else {
            team.getTeamPlayer().remove(player.getId());
        }

        if (team.getTeamPlayer().size()==0){
            teamCache.clearTeam(teamId);
        }

    }

    public void showTeam(Player player){
        Integer teamId=player.getTeamId();
        Team team=teamCache.getCacheTeam(teamId);
        if(null==team){
            notify.notifyPlayer(player,"没加入队伍");
            return ;
        }
        team.getTeamPlayer().values().forEach(playerTmp -> {
            if(playerTmp.getId().equals(team.getCaptainId())){
                notify.notifyPlayer(player,MessageFormat.format("队长为{0},id为{1},等级为{2},hp为{3}",
                        playerTmp.getName(),playerTmp.getId(),playerTmp.getLevel(),playerTmp.getHp()));
            }else {
                notify.notifyPlayer(player,MessageFormat.format("队员为{0},id为{1},等级为{2},hp为{3}",
                        playerTmp.getName(),playerTmp.getId(),playerTmp.getLevel(),playerTmp.getHp()));
            }
        });

    }

}
