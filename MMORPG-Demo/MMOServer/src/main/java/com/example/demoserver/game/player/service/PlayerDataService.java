package com.example.demoserver.game.player.service;



import com.example.demoserver.common.commons.Character;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.server.notify.Notify;
import com.example.demoserver.timejob.TimeTaskThreadManager;
import com.google.common.base.Strings;

import com.sun.xml.internal.bind.v2.TODO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;



@Service
@Slf4j
public class PlayerDataService {


    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    //用于回写客户端
    @Autowired
    private Notify notify;

    @Autowired
    private GameSceneService gameSceneService;


    /**
     *  通过上下文查找玩家
     * @param ctx   上下文
     * @return  玩家
     */
    public Player getPlayerByCtx(ChannelHandlerContext ctx) {

        return playerCacheMgr.getPlayerByCtx(ctx);
    }


    /**
     *  通过玩家id查找玩家，如果玩家不在线，从数据库中寻找
     * @param playerId  玩家id
     * @return 如果存在玩家记录则返回玩家，不存在则返回空，注意玩家可能尚未初始化
     */
    public Player getPlayerById(long playerId) {

        ChannelHandlerContext ctx = playerCacheMgr.getCxtByPlayerId(playerId);
        if(Objects.nonNull(ctx)) {
            return getPlayerByCtx(ctx);
        } else {
              // TPlayer tPlayer = findTPlayer(playerId);
             /*  if (Objects.nonNull(tPlayer)) {
                   Player player = new Player();
                   BeanUtils.copyProperties(tPlayer,player);
                   return player;*/
            return null;
        }/* else {
                   return null;
               }*/

        }

    /**
     *  通过玩家id查找在线玩家
     * @param playerId  玩家id
     * @return 如果玩家在线则返回玩家，如果不在线则返回空
     */
    public Player getOnlinePlayerById(long playerId) {
        ChannelHandlerContext ctx = playerCacheMgr.getCxtByPlayerId(playerId);
        if(null != ctx) {
            return getPlayerByCtx(ctx);
        } else {
            return null;
        }
    }


    /**
     *   通过数据库查找玩家
     * @param //playerId 玩家id
     * @return 数据库的玩家实体
     */
    TODO 数据库方法;


    public Player getPlayer(ChannelHandlerContext ctx) {
        return playerCacheMgr.getPlayerByCtx(ctx);
    }


    /**
     *     检测玩家是否死亡
     * @param player 伤害承受者
     * @param murderer 攻击发起者
     */

        public synchronized boolean isPlayerDead(Player player, Character murderer)  {

        if (player.getHp() <= 0){

            player.setHp((long)0);

            player.setState(-1);

            String message=MessageFormat.format("玩家 {0} 被 {1} 杀死  \n",
                    player.getName(),murderer.getName());

            // 广播并通知死亡的玩家

            notify.notifyScene(player.getCurrentScene(),message);

            gameSceneService.moveToScene(player,1);
            notify.notifyPlayer(player,player.getName()+"  你已经在起始之地了,十秒后复活 \n");

            try {
                TimeTaskThreadManager.threadPoolSchedule(
                        10000, () -> {
                            player.setState(1);
                            initPlayer(player);
                            notify.notifyPlayer(player,player.getName()+"  你已经复活 \n");
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }

    }


    /**
     *  初始化角色, 注意，加载循序不能错。
     * @param player 角色
     */
   public void initPlayer(Player player) {

        // 加载到场景中
        gameSceneService.initPlayerScene(player);

        log.debug("player {}", player);

    }



}
