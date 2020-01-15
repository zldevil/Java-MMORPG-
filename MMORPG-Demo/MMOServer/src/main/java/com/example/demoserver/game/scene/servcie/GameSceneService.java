package com.example.demoserver.game.scene.servcie;

import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scene.manager.SceneCacheMgr;
import com.example.demoserver.game.scene.model.GameScene;

import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class GameSceneService {


    @Autowired
    private PlayerDataService playerDataService;


    @Autowired
    private Notify notify;

    /**
     *      通过玩家得到现在所在场景
     * @param玩家
     * @return
     */

    public GameScene getSceneByPlayer(Player player) {

        player.setSenceId(player.getCurrentScene().getId());

        return player.getCurrentScene();
}

    /**
     *   通过字符串的id序列查找场景
     */
    public List<GameScene> getNeighborsSceneByIds(String sceneIds) {
        List<GameScene> gameSceneList = new ArrayList<>();
        if (Objects.isNull(sceneIds) || sceneIds.isEmpty()) {
            return gameSceneList;
        }
        String[] stringIds = sceneIds.split(",");
        Arrays.stream(stringIds).forEach((stringId) -> {
            Integer id = Integer.valueOf(stringId);
            GameScene gameScene = SceneCacheMgr.getScene(id);
            gameSceneList.add(gameScene);
        });
        return gameSceneList;
    }

    /**
     *  通过玩家寻找相邻场景
     * @param player 玩家
     * @return 相邻的场景
     */
    private List<GameScene> getNeighborsSceneByPlayer(Player player) {
        GameScene gameScene = getSceneByPlayer(player);
        //string类型
        return getNeighborsSceneByIds(gameScene.getNeighborScence());
    }

    /**
     *      通过上下文查找场景
     * @param ctx 通道上下文
     * @return 该通道当前的场景
     */
    public GameScene getSceneByCtx(ChannelHandlerContext ctx) {
        Player player = playerDataService.getPlayer(ctx);
        return getSceneByPlayer(player);
    }

    /**
     *  传送进某个场景
     * @param player 玩家
     * @param sceneId 目的场景的id
     */

    public void moveToScene(Player player, int sceneId) {
        GameScene gameScene = getSceneByPlayer(player);
        // 从当前场景移除
        gameScene.getPlayers().remove(player.getId());

        player.setSenceId(sceneId);
        GameScene targetScene = SceneCacheMgr.getScene(sceneId);
        // 放入目的场景
        targetScene.getPlayers().put(player.getId(), player);
        player.setCurrentScene(targetScene);


        notify.playerEnter(player,targetScene);
    }

    /**
     *  进入场景初始化
     * @param player 玩家
     */

    public void initPlayerScene(Player player) {

        GameScene scene = SceneCacheMgr.getScene(player.getSenceId());

        if (Objects.isNull(scene)) {

            scene = SceneCacheMgr.getScene(1);
        }

        scene.getPlayers().put(player.getId(),player);
        player.setCurrentScene(scene);

        // 广播
        notify.playerEnter(player,scene);

    }

    /**
     *  是否能移动到一个场景
     * @param player player
     * @param willGo 想要去的场景
     * @return 是否移动成功
     */
    public boolean canMoveTo(Player player, GameScene willGo) {

        List<GameScene> gameSceneList = getNeighborsSceneByPlayer(player);
        // 检测是否能够到达想去的地方
        return  gameSceneList.stream().anyMatch(scene -> scene.getId().equals(willGo.getId()));
    }


}
