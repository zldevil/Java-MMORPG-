package com.example.demoserver.game.SceneEntity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.game.SceneEntity.model.Drop;
import com.example.demoserver.game.SceneEntity.model.ScenceEntity;
import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataBaseService;
import com.example.demoserver.server.notify.Notify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class MonsterDropItemService {


    @Resource
    private BagService bagService;

    @Resource
    private Notify notify;

    @Autowired
    private ItemInfoCache itemInfoCache;

    @Autowired
    private PlayerDataBaseService playerDataBaseService;



    /**
     * 物品掉落
     */
    //进行数据库操作，更新玩家信息
    void dropItem(Player player, ScenceEntity scenceEntity) {

        // 掉落经验，先固定经验值
        player.addExp(20);

        List<ItemInfo> dropItemInfoList=calculateDrop(scenceEntity);

        Bag bag = player.getBag();
        if (Objects.isNull(dropItemInfoList)){
            return;
        }

        dropItemInfoList.forEach(itemInfo -> {
            Long itemId = bagService.generateItemId();
            Item item = new Item(itemId, 1, itemInfo);
            item.setItemInfo(itemInfo);
            if (!bagService.addItem(player, item)) {
                notify.notifyPlayer(player,
                        MessageFormat.format("背包已满，放不下物品 {0}", itemInfo.getName()));
            }else {
                notify.notifyPlayer(player,
                        MessageFormat.format("掉落的{0}已放入背包", itemInfo.getName()));
            }

        });

      playerDataBaseService.updatePlayerDataBase(player);

    }



    /**
     * 计算物品掉落,获得
     */
    public List<ItemInfo> calculateDrop(ScenceEntity scenceEntity ) {
       String dropString = scenceEntity.getDropItems();

       List<Drop> drops=JSON.parseObject(dropString,new TypeReference<List<Drop>>(){});

       List<ItemInfo> resultDropItem=new ArrayList<>() ;
        Random random=new Random();
       drops.forEach(drop -> {
           int ran=random.nextInt(100);
           if(ran/100<drop.getProbability()){

               resultDropItem.add(itemInfoCache.get(drop.getItemId()));
           }
       });

       return resultDropItem;

    }


}
