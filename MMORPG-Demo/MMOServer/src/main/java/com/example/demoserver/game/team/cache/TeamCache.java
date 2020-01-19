package com.example.demoserver.game.team.cache;

import com.example.demoserver.game.team.model.Team;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TeamCache {


    private static Cache<Integer, Team> teamCache = CacheBuilder.newBuilder()
            // 设置并发级别，最多8个线程同时写
            .concurrencyLevel(10)
            // 设置缓存容器的初始容量为100
            .initialCapacity(100)
            .maximumSize(5000)
            .recordStats()
            .removalListener(
                    notification -> System.out.println(notification.getKey() + "队伍被移除, 原因是" + notification.getCause())
            ).build();


    public void put(Integer teamId,Team team){
        teamCache.put(teamId,team);
    }

    public Team getCacheTeam(Integer teamId){
        return teamCache.getIfPresent(teamId);
    }

    public void clearTeam(Integer teamId){
        teamCache.invalidate(teamId);
    }
}
