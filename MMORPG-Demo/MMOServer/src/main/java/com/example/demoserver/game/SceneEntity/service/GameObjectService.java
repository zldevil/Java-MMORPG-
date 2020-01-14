package com.example.demoserver.game.SceneEntity.service;


import com.example.demoserver.game.SceneEntity.manager.GameObjectManager;
import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.SceneEntity.model.NPC;
import com.example.demoserver.game.SceneEntity.model.ScenceEntity;
import com.example.demoserver.game.SceneEntity.model.SceneObjectType;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scene.model.GameScene;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;


@Service
public class GameObjectService {


    @Resource
    private GameObjectManager gameObjectManager;


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
     * @param monster 场景对象死亡后处理
     * @return
     */
    public boolean sceneObjectAfterDead(Monster monster) {
        if (monster.getHp() <= 0) {
            // 重要，设置死亡时间
            monster.setDeadTime(System.currentTimeMillis());
            monster.setHp(0L);
            monster.setState(-1);
            // 重要，清空对象当前目标
            monster.setTarget(null);
            return true;
        } else{
            return false;
        }
    }


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

        ScenceEntity scenceEntity = gameObjectManager.get(id_NPC);
       /* NPC npc =new NPC();
        BeanUtils.copyProperties(scenceEntity,npc);*/
        Notify.notifyByCtx(ctx,scenceEntity.getTalk());

    }

}
