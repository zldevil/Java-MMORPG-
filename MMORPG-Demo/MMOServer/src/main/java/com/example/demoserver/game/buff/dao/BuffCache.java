package com.example.demoserver.game.buff.dao;

import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.player.model.Role;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class BuffCache {

    public static Cache<Integer, Buff> buffCache= CacheBuilder.newBuilder()
            .removalListener(
                    notification -> log.debug(notification.getKey() + "移除,原因是" + notification.getCause()))
            .build();

    @PostConstruct
    public void init(){
        try {
            List<Buff> buffList= ExcelUtil.readExcel("static/buff.xlsx",new Buff());
            buffList.forEach(buff -> {
                buffCache.put(buff.getId(),buff);
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Buff读取异常");
        }

        log.info("Buff表读取成功");

    }

    public Buff get(Integer bufferId) {
        return buffCache.getIfPresent(bufferId);
    }


    public void put(Integer bufferId, Buff buff) {
        buffCache.put(bufferId,buff);
    }
}
