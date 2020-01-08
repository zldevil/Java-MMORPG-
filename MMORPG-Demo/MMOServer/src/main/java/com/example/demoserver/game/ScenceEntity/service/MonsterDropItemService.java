package com.example.demoserver.game.ScenceEntity.service;

import com.alibaba.fastjson.JSON;
import com.example.demoserver.game.ScenceEntity.model.Drop;
import com.example.demoserver.game.ScenceEntity.model.ScenceEntity;
import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.server.notify.Notify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MonsterDropItemService {


    @Resource
    private BagService bagService;

    @Resource
    private Notify notify;

    @Autowired
    private ItemInfoCache itemInfoCache;


    /**
     * 物品掉落
     */
    void dropItem(Player player, ScenceEntity scenceEntity) {

        /*// 掉落金钱，此处默认按怪物mp掉钱
        player.moneyChange(scenceEntity.getMp().intValue());


        // 掉落经验，，此处默认按怪物mp来计算
        player.addExp(scenceEntity.getMp().intValue());

        Bag bag = player.getBag();
        List<Drop> dropList = calculateDrop();
        if (Objects.isNull(dropList))
            return;
        for (Drop drop : dropList) {
            log.debug("drop {}", drop);
            int chance = drop.getChance();
            boolean flag = ProbabilityUtil.dropProbability(chance);
            if (flag) {
                ItemInfo itemInfo = itemInfoCache.get(drop.getThingId());
                // 随机的物品id
                Long itemId = thingInfoService.generateItemId();
                Item item = new Item(itemId,1,thingInfo);
                item.setThingInfo(thingInfo);
                if (!bagService.addItem(player,item)) {
                    notify.notifyPlayer(player,
                            MessageFormat.format("背包已满，放不下物品 {0}", thingInfo.getName()));
                }
                log.debug("bag {}", bag);
            }
        }*/
    }



    /**
     * 计算物品掉落,获得
     */
    /*public List<Drop> calculateDrop(ScenceEntity scenceEntity ) {
       String dropString = scenceEntity.getDrop();
        return JSON.parseArray(dropString, Drop.class);
    }*/


}
