package com.example.demoserver.game.bag.dao;

import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.example.demoserver.server.notify.Notify;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemInfoCache {


    @Autowired
    private BagService bagService;

    private static Cache<Integer, ItemInfo> itemInfoCache = CacheBuilder.newBuilder()
            .recordStats()
            .removalListener(
                    notification -> log.debug(notification.getKey() + "物品被移除, 原因是" + notification.getCause())
            ).build();


    @PostConstruct
    private void init() {
        try {
            List<ItemInfo> itemInfoList = ExcelUtil.readExcel("static/item.xlsx",new ItemInfo());

            itemInfoList.forEach(itemInfo -> {
                itemInfoCache.put(itemInfo.getId(),itemInfo);
            });

            log.info("物品配置表加载完毕");

        } catch (Exception e) {
            e.printStackTrace();
        }

        }


    public ItemInfo get(Integer id) {
        ItemInfo itemInfo = itemInfoCache.getIfPresent(id);
        // 如果属性集合为空，加载物品属性
/*
        if (itemInfo != null) {
            bagService.loadThingsProperties(itemInfo);
        }*/
        return itemInfo;
    }


    public void put(Integer id, ItemInfo thingInfo) {
        itemInfoCache.put(id, thingInfo);
    }
    
}
