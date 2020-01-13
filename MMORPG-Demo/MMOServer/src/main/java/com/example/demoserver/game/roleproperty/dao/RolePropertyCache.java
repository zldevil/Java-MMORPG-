package com.example.demoserver.game.roleproperty.dao;

import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class RolePropertyCache {

    public  static Cache<Integer, RoleProperty> rolePropertyCache = CacheBuilder.newBuilder().
            removalListener(
            notification -> log.debug(notification.getKey() + "被移除,原因是" + notification.getCause())
    ).build();

    @PostConstruct
    public void init(){
        try {
            List<RoleProperty> rolePropertyList= ExcelUtil.readExcel("static/roleProperties.xlsx",new RoleProperty());

            rolePropertyList.forEach(roleProperty -> {
                rolePropertyCache.put(roleProperty.getId(),roleProperty);
            });

            log.info("角色属性资源加载完成");

        } catch (Exception e) {
            e.printStackTrace();
            log.info("角色属性加载失败");
        }


    }

    public RoleProperty get(Integer rolePropertyId){
        return rolePropertyCache.getIfPresent(rolePropertyId);
    }

    public void put(Integer rolePropertyId,RoleProperty roleProperty){
        rolePropertyCache.put(rolePropertyId,roleProperty);
    }
}
