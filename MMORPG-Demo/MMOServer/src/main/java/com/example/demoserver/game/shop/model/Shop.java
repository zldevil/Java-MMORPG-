package com.example.demoserver.game.shop.model;

import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Shop {

    @Autowired
    private BagService bagService;
    @Autowired
    private ItemInfoCache itemInfoCache;



    private Integer shopId;

    private String name;

    private String goodsOnSale;




    private Map<Integer, ItemInfo> goodsMap = new ConcurrentHashMap<>();


    public Map<Integer,ItemInfo> getGoodsOnSaleMap(){
        String[] itemInfoId = goodsOnSale.split(",");
        for (String itemInfoTmpId:itemInfoId){
           ItemInfo itemInfo= itemInfoCache.get(Integer.valueOf(itemInfoTmpId));
           goodsMap.put(itemInfo.getId(),itemInfo);
        }
        return goodsMap;
    }

}
