package com.example.demoserver.game.ScenceEntity.service;


import com.example.demoserver.game.ScenceEntity.manager.GameObjectManager;
import com.example.demoserver.game.ScenceEntity.model.Monster;
import com.example.demoserver.game.ScenceEntity.model.NPC;
import com.example.demoserver.game.ScenceEntity.model.ScenceEntity;
import com.example.demoserver.game.ScenceEntity.model.SceneObjectType;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class GameObjectService {


    @Resource
    private GameObjectManager gameObjectManager;

   /* @Resource
    private NotificationManager notificationManager;*/

    @Resource
    private PlayerDataService playerDataService;



    public ScenceEntity getGameObject(Integer gameObjectId) {
        return gameObjectManager.get(gameObjectId);
    }


    public void cacheGameObject(Integer gameObjectId, ScenceEntity sceneObject) {
        gameObjectManager.put(gameObjectId, sceneObject);
    }


    /**
     *  场景对象死亡后处理
     * @param sceneObject 场景对象死亡后处理
     * @return
     */
    /**public boolean sceneObjectAfterDead(SceneObject sceneObject) {
        if (sceneObject.getHp() <= 0) {
            // 重要，设置死亡时间
            sceneObject.setDeadTime(System.currentTimeMillis());
            sceneObject.setHp(0L);
            sceneObject.setState(-1);
            // 重要，清空对象当前目标
            sceneObject.setTarget(null);
            return true;
        } else{
            return false;
        }
    }
*/
    /**
     *      获得场景配置的场景对象
     */

    public GameScene initSceneObject(GameScene gameScene) {

        String  gameObjectIds = gameScene.getScenceEntityId();

        Arrays.stream(gameObjectIds.split(","))
                // 转 换 类 型
                .map(Integer::valueOf)
                .map( this::getGameObject)
                .forEach( scenceEntity -> {
                    if ( scenceEntity.getRoleType().equals(SceneObjectType.NPC.getType())) {
                        NPC npc = new NPC();
                        //通过工具类复制对象
                       BeanUtils.copyProperties(scenceEntity,npc);
                        gameScene.getNpcs().put(scenceEntity.getId(), npc);
                    }
                    if (scenceEntity.getRoleType().equals(SceneObjectType.WILD_MONSTER.getType()) ) {
                        Monster monster = new Monster();
                       BeanUtils.copyProperties(scenceEntity,monster);
                        gameScene.getMonsters().put(scenceEntity.getId(), monster);
                    }
                }
        );
        return gameScene;
    }


    public void talkWithNpc(Integer id_NPC,ChannelHandlerContext ctx){

        NPC npc =(NPC) gameObjectManager.get(id_NPC);
        Notify.notifyByCtx(ctx,npc.getTalk());

    }

}
