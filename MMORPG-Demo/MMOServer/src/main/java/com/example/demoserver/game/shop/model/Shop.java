package com.example.demoserver.game.shop.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Shop {


    private Integer shopId;

    private String name;

    private String goodsOnSale;


    private Map<Integer, GoodsOnSale> goodsMap = new ConcurrentHashMap<>();


    public Map<Integer,GoodsOnSale> getGoodsOnSaleMap(){

        List<GoodsOnSale> goodsOnSaleList = JSON.parseObject(goodsOnSale,new TypeReference<List<GoodsOnSale>>(){});
        goodsOnSaleList.forEach(goodsOnSaleTmp ->{
            goodsMap.put(goodsOnSaleTmp.getItemId(),goodsOnSaleTmp);
        } );
        return goodsMap;
    }

}
