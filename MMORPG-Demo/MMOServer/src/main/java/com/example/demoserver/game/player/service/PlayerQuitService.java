package com.example.demoserver.game.player.service;



import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Description: 角色退出服务
 */
@Service
public class PlayerQuitService  {



    @Autowired
    private PlayerCacheMgr playerCacheMgr;


    @Autowired
    private Notify notify;

    @Autowired
    private PlayerDataService playerDataService;


    @Autowired
    private GameSceneService gameSceneService;

    /**
     *  主动注销当前角色
     */
    public void logout(ChannelHandlerContext ctx) {
        Player player =playerDataService.getPlayerByCtx(ctx);

        savePlayer(ctx);

        // 从场景退出
        logoutScene(ctx);

        // 主动退出游戏的清除其缓存
        //cleanPlayerCache(ctx);

    }




    /**
     *  退出场景
     */

    public void logoutScene(ChannelHandlerContext ctx) {
        Player player = playerDataService.getPlayerByCtx(ctx);

        Optional.ofNullable(player).ifPresent(
                p -> {
                    GameScene gameScene = gameSceneService.getSceneByCtx(ctx);

                   /* notify.notifyScene(gameScene,
                            MessageFormat.format("玩家 {0} 正在退出", player.getName()));*/

                    // 重点，从场景中移除
                    gameScene.getPlayers().remove(player.getId());
                }
        );

    }





    /**
     *  清除与角色相关的缓存
     */

    public void cleanPlayerCache(ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asLongText();

        Player player = playerDataService.getPlayerByCtx(ctx);
        if (player != null) {
            // 移除角色所有的缓存
            playerCacheMgr.removePlayerCxt(player.getId());
            playerCacheMgr.removePlayerByChannelId(channelId);
        }
    }



    /**
     *  保存角色所有数据
     */
    public void savePlayer(ChannelHandlerContext ctx) {
        Player player = playerCacheMgr.getPlayerByCtx(ctx);

        // 保存角色信息
        if (player != null) {
            // 持久化角色信息
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(player, userEntity);


        }
        }
    }


