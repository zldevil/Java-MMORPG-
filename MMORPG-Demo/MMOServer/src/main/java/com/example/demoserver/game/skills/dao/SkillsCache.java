package com.example.demoserver.game.skills.dao;

import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class SkillsCache {

    public static Cache<Integer, Skill> skillCache= CacheBuilder.newBuilder().removalListener(
            notification -> log.debug(notification.getKey() + "物品被移除, 原因是" + notification.getCause())
    ).build();

    @PostConstruct
    public void init(){
        List<Skill> skillList= null;
        try {
            skillList = ExcelUtil.readExcel("static/skills.xlsx",new Skill());
        } catch (Exception e) {
            e.printStackTrace();
        }
        skillList.forEach(skill -> {
            skillCache.put(skill.getId(),skill);
        });
    }

    public  Skill get(Integer skillId){
        return skillCache.getIfPresent(skillId);
    }

    public void set(Integer skillId,Skill skill){
        skillCache.put(skillId,skill);
    }
}
