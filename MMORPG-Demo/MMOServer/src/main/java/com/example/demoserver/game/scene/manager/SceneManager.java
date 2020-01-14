package com.example.demoserver.game.scene.manager;


import com.example.demoserver.game.SceneEntity.manager.GameObjectManager;
import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.SceneEntity.model.ScenceEntity;
import com.example.demoserver.game.scene.model.GameScene;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Description: 场景心跳定时器
 */

@Component
@Slf4j

public class SceneManager {


    // 单线程定时执行器
    private static ThreadFactory sceneLoopThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("sceneLoop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();

    //定时任务
    private static ScheduledExecutorService sceneLoop =
            Executors.newSingleThreadScheduledExecutor(sceneLoopThreadFactory);


    @Autowired
    private GameObjectManager gameObjectManager;





    //周期性
    @PostConstruct
    private void tick() {
        sceneLoop.scheduleWithFixedDelay(
                this::refreshScene,
                1000, 60, TimeUnit.MILLISECONDS);
        log.debug("场景定时器启动");
    }


    private void refreshScene() {
        List<GameScene>  gameSceneList= SceneCacheMgr.list();
        for (GameScene gameScene : gameSceneList) {

            // 刷新怪物
            gameScene.getMonsters().values().forEach(this::refreshDeadCreature);

            // 设置怪物攻击
            gameScene.getMonsters().values().forEach(monster -> monsterAttack(monster,gameScene));

        }
    }

   /* *
     *  刷新死亡的生物
     * @param scenceEntity  场景中的非玩家生物*/
    private void refreshDeadCreature(ScenceEntity scenceEntity) {
        if (scenceEntity.getState() == -1 &&
                //15秒暂时设定
                scenceEntity.getDeadTime()+ 15000< System.currentTimeMillis()) {
            ScenceEntity sceneObject = gameObjectManager.get(scenceEntity.getId());
            scenceEntity.setHp(sceneObject.getHp());
            scenceEntity.setState(sceneObject.getState());
        }
    }


   /**
     *  刷新怪物攻击
     */

    private void monsterAttack(Monster monster, GameScene gameScene) {

        if (Objects.nonNull(monster.getTarget())) {
           // monsterService.startAI(monster,gameScene);
        }
    }


}
