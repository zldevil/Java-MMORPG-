package com.example.demoserver.game.ScenceEntity.manager;

import com.example.demoserver.game.ScenceEntity.model.ScenceEntity;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class GameObjectManager  {


    private static Cache<Integer, ScenceEntity> gameEntityCache = CacheBuilder.newBuilder()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "游戏对象被移除，原因是" + notification.getCause())
            ).build();


    @PostConstruct
    public void init() throws Exception {

        List<ScenceEntity> scenceEntityList = ExcelUtil.readExcel("static/SceneEntity.xlsx",new ScenceEntity());

        scenceEntityList.forEach(scenceEntitytmp ->{
            gameEntityCache.put(scenceEntitytmp.getId(),scenceEntitytmp);
        } );

        log.info("游戏对象资源加载完毕");
    }


    public ScenceEntity get(Integer gameObjectId) {
        return gameEntityCache.getIfPresent(gameObjectId);
    }

    public void put(Integer gameObjectId, ScenceEntity value) {
        gameEntityCache.put(gameObjectId,value);
    }

    public Map<Integer,ScenceEntity> list() {
        return gameEntityCache.asMap();
    }
}
