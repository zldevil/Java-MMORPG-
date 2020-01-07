package com.example.demoserver.game.scence.manager;

import com.example.demoserver.game.ScenceEntity.service.GameObjectService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Slf4j
@Service

public class SceneCacheMgr  {


    /** 缓存不过期 **/
    private static Cache<Integer, GameScene> sceneCache = CacheBuilder.newBuilder()
            .removalListener(

                    notification -> System.out.println(notification.getKey() + "场景被移除, 原因是" + notification.getCause())

            ).build();



    @Autowired
    private GameObjectService gameObjectService;


    @PostConstruct
    private void init() throws Exception {


        List<GameScene> listOfScence = ExcelUtil.readExcel("static/Scene.xlsx",new GameScene());

        listOfScence.forEach( gameScene1 -> {
            gameScene1=gameObjectService.initSceneObject(gameScene1);
            sceneCache.put(gameScene1.getId(),gameScene1);

        });

        log.info("场景资源加载进缓存完毕");
    }



    public static GameScene getScene(Integer scenceKey) {
        return sceneCache.getIfPresent(scenceKey);
    }



    public static List<GameScene> list() {
        List<GameScene> gameScenesList = new ArrayList<>();
        Map<Integer,GameScene>  sceneCacheMap = sceneCache.asMap();

        for (Map.Entry <Integer,GameScene> gameSceneEntry : sceneCacheMap.entrySet()) {
            gameScenesList.add(gameSceneEntry.getValue());
        }
        return gameScenesList;
    }


}
