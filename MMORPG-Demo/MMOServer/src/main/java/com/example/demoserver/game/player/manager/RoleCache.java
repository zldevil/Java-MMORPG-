package com.example.demoserver.game.player.manager;

import com.example.demoserver.game.player.model.Role;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class RoleCache {


    public static Cache<Integer, Role> roleCache= CacheBuilder.newBuilder()
            .removalListener(
                    notification -> log.debug(notification.getKey() + "职业被移除, 原因是" + notification.getCause()))
            .build();




    @PostConstruct
    public void init() throws Exception {
        //通过POI读取excel加载到

        List<Role> roleList = ExcelUtil.readExcel("static/role.xlsx",new Role());
        roleList.forEach(role -> {
            roleCache.put(role.getId(),role);
        });

        log.info("角色职业数据加载完毕");
    }


    public static Role getRole(Integer roleId) {

        return roleCache.getIfPresent(roleId);
    }

}
