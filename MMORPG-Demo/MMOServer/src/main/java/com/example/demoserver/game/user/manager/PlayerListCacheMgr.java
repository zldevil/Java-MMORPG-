package com.example.demoserver.game.user.manager;

import com.example.demoserver.game.player.model.UserEntity;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * Description: 角色列表缓存
 */
@Slf4j
@Component
public class PlayerListCacheMgr  {



    /**
     * Integer为user的id
    * */
    private static Cache<Integer, List<UserEntity>> playerListCache = CacheBuilder.newBuilder()
            // 设置写缓存后，三小时过期
            .expireAfterWrite(3, TimeUnit.HOURS)
            .removalListener(
                    notification -> log.info(notification.getKey() + "被移除, 原因是" + notification.getCause())
            ).build();


    public List<UserEntity> get(int userId) {

        return playerListCache.getIfPresent(userId);
    }


    //user-用户，player-角色
    public void put(int userId, List<UserEntity> playerList) {

        playerListCache.put(userId, playerList);
    }
}
