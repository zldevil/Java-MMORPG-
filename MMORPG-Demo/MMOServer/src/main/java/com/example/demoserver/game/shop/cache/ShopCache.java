package com.example.demoserver.game.shop.cache;

import com.example.demoserver.game.shop.model.Shop;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class ShopCache {

    public static Cache<Integer, Shop> shopCache = CacheBuilder.newBuilder().removalListener(
            notification -> System.out.println(notification.getKey() + "商店被移除，原因是" + notification.getCause())
    ).build();


    @PostConstruct
    public void init(){

        try {
            List<Shop>  shopList = ExcelUtil.readExcel("static/shop.xlsx",new Shop());
            shopList.forEach(shop -> {
                shopCache.put(shop.getShopId(),shop);
                log.info("商品加载成功");
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.info("商品加载失败");
        }

    }

    public Shop getShop(Integer shopId){
        return shopCache.getIfPresent(shopId);
    }

}
