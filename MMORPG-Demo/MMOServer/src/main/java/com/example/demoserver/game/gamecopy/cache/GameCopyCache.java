package com.example.demoserver.game.gamecopy.cache;

import com.example.demoserver.game.gamecopy.model.GameCopy;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GameCopyCache {

    public static Cache<Integer, GameCopy> gameCopyCache = CacheBuilder.newBuilder().removalListener(
            notification->log.info("被移除")
    ).build();

    @Constructor
    public void init(){
        List<GameCopy> gameCopyConfigList= null;
        try {
            gameCopyConfigList = ExcelUtil.readExcel("static/gamecopy.xlsx",new GameCopy());
            gameCopyConfigList.forEach(gameCopyConfig -> {
                    gameCopyCache.put(gameCopyConfig.getId()
                    ,gameCopyConfig);
            });
            log.info("副本场景配置读取成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("副本资源读取失败");
        }

    }

    public GameCopy get(Integer id){
        return gameCopyCache.getIfPresent(id);
    }

}
