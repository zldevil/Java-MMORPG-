package com.example.demoserver.game.guild.cache;

import com.example.demoserver.game.guild.model.Guild;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class GuildCache {

    public static Cache<String, Guild> nameGuildCache = CacheBuilder.newBuilder().removalListener(
            notification->log.info("被移除的原因")
    ).build();


    public static Cache<Integer, Guild> idGuildCache = CacheBuilder.newBuilder().removalListener(
            notification->log.info("被移除的原因")
    ).build();

    //@PostConstruct
    public void init(){

    }

    public Guild get(String guildName){
        return nameGuildCache.getIfPresent(guildName);
    }

    public Guild getGuildById(Integer id){
        return idGuildCache.getIfPresent(id);
    }

}
