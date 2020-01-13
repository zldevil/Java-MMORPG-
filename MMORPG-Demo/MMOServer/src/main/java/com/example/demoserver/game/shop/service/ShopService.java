package com.example.demoserver.game.shop.service;

import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.shop.cache.ShopCache;
import com.example.demoserver.game.shop.model.GoodsOnSale;
import com.example.demoserver.game.shop.model.Shop;
import com.example.demoserver.server.notify.Notify;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collector;

@Service
@Slf4j
public class ShopService {

    @Autowired
    private ShopCache shopCache;

    @Autowired
    private Notify notify;

    @Autowired
    private BagService bagService;

    @Autowired
    private ItemInfoCache itemInfoCache;

    public void buyGood(Player player,Integer shopId,Integer itemId,Integer num){

        Shop shop =  shopCache.getShop(shopId);
        GoodsOnSale goodsOnSale =shop.getGoodsMap().get(itemId);

        ItemInfo itemInfo = itemInfoCache.get(goodsOnSale.getItemId());
        if ( null == itemInfo){
            notify.notifyPlayer(player,"该商店没有此货物");
        }

        if (player.getMoney() < itemInfo.getPrice()) {

            notify.notifyPlayer(player,"不好意思，你身上的钱不足以购买该物品");

        }

        Item item = new Item(bagService.generateItemId(),2, itemInfo);

        log.debug("商店 shop {}",shop);

        if(bagService.addItem(player,item)) {
            // 商品放入背包成功，扣钱
            player.moneyChange(itemInfo.getPrice());
            notify.notifyPlayer(player, MessageFormat.format("购买{0}成功",itemInfo.getName()));

        } else {

            notify.notifyPlayer(player,"购买失败，可能是因为背包已满");
        }

    }


    public  void showGoodsOnShop(Player player,Integer shopId){
        Shop shop =shopCache.getShop(shopId);

        List<GoodsOnSale> goodsOnSaleList =(List<GoodsOnSale>) shop.getGoodsMap().values();

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(" 商店中有: ");
        goodsOnSaleList.forEach(goodsOnSale -> {
            ItemInfo itemInfo=itemInfoCache.get(goodsOnSale.getItemId());
            stringBuilder.append(MessageFormat.format("商品名称为:{0},id为{1}",itemInfo.getName(),itemInfo.getId()));
        });

        notify.notifyPlayer(player,stringBuilder);

    }

}
