package com.example.demoserver.game.bag.service;

import com.alibaba.fastjson.*;
import com.example.demoserver.common.commons.Constant;
import com.example.demoserver.game.bag.dao.BagMapper;
import com.example.demoserver.game.bag.dao.ItemInfoCache;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.model.ItemType;
import com.example.demoserver.game.buff.dao.BuffCache;
import com.example.demoserver.game.buff.service.BuffService;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.roleproperty.service.RolePropertyService;
import com.example.demoserver.server.notify.Notify;
import com.google.common.base.Strings;
import com.sun.xml.internal.bind.v2.TODO;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import java.text.MessageFormat;
import java.util.*;

@Service
@Slf4j
public class BagService {


    @Autowired
    private ItemInfoCache itemInfoCache;

    @Autowired
    private RolePropertyService rolePropertyService;

    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    @Autowired
    private BuffService buffService;

    @Autowired
    private BuffCache buffCache;

    @Autowired
    private  Notify notify;

    @Autowired
    private BagMapper bagMapper;


    public void packBag(ChannelHandlerContext ctx){

    }

    public Map show(Player player){

        Bag bag =player.getBag();

        return bag.getItemMap();

    }

    /**
     *  加载物品的属性内容
     * @param itemInfo 物品
     */


    /**
     *  获取物品描述
     * @param itemInfoId 物品id
     */
    public ItemInfo getItemInfo(Integer itemInfoId) {

        return itemInfoCache.get(itemInfoId);

    }

    /**
     *
     * @param player
     * @param itemId
     * @param count
     * @return
     */
    public boolean useItem(Player player, Integer itemId, Integer count) {

        ChannelHandlerContext channelHandlerContext= playerCacheMgr.getCxtByPlayerId(player.getId());

        Bag bag = player.getBag();

        Item item = bag.getItemMap().get(itemId);

        //BUFF的列代表是装备还是消耗品
        if(item == null  || item.getItemInfo().getBuff() == null) {
            Notify.notifyByCtx(channelHandlerContext,"该物品不属于消耗品");
            return false;
        }
        if(item.getCount() < count) {
            Notify.notifyByCtx(channelHandlerContext,"消耗品数量不足");

            return false;
        }

        ItemInfo itemInfo = itemInfoCache.get(item.getItemInfo().getId());

        String notice=MessageFormat.format("开始使用 : {0}",itemInfo.getName());
        Notify.notifyByCtx(channelHandlerContext,notice);

        /**
         * Constant.EXPITEM_BUFF 经验药
         */
        if(itemInfo.getBuff().equals(Constant.EXPITEM_BUFF)) {
            player.addExp(count);
        } else {
            for (int i = 0; i < count; i++) {


                Optional.ofNullable(buffCache.get(itemInfo.getBuff()))
                        .ifPresent(buff -> {
                            try {
                                buffService.startBuffer(player,buff);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

            }
        }
        return removeItem(player, item.getId(), count);
    }



    /**
     * 移除物品
     * @param player
     * @param itemId
     * @return
     */
    public boolean removeItem(Player player, Long itemId, Integer count) {
        Bag bag = player.getBag();
        Map<Long, Item> itemMap = bag.getItemMap();
        if(!itemMap.isEmpty()) {
            Item item = bag.getItemMap().get(itemId);

            String itemName = item.getItemInfo().getName();

            //非装备 同时数量够减
            if( item.getCount() > count) {
                item.setCount(item.getCount() - count);
            }else {
                if(!itemMap.remove(itemId, item)) {
                    notify.notifyPlayer(player,"物品移除失败");
                    return false;
                }
            }

            notify.notifyPlayer(player,MessageFormat.format("背包中 {0} 减少了 {1} 个",itemName,count));

            //更新数据库？还是最后更新数据库
            return true;
        }
        return false;
    }

    /**
     *  从背包中寻找空位置放进去
     * @param player 玩家
     * @param item 物品
     * @return 物品是否放入背包成功
     */

    public boolean addItem(Player player, Item item) {

        Bag bag = player.getBag();
        if (item == null) {
            return false;
        }

        Boolean flag=true;

        Map<Long, Item> itemMap = bag.getItemMap();

        // 种类为2的物品为可堆叠的

        if (item.getItemInfo().getType().equals(ItemType.CONSUMABLE_ITEM.getType())) {


            if (itemMap.size() >= Constant.packageSize) {
                if (itemMap.get(item.getId()) == null) {
                    notify.notifyPlayer(player, "背包满，物品无法放进背包");
                    flag=false;
                } else {
                    itemMap.put(item.getId(), item);
                }
            } else {
                if (itemMap.get(item.getId()) == null) {
                    itemMap.put(item.getId(), item);
                } else {
                    Optional.ofNullable(itemMap.get(item.getId())).ifPresent(itemTmp -> {
                        itemTmp.setCount(itemTmp.getCount() + 1);
                    });
                }

            }
            /*for (int locationIndex=1; locationIndex <= bag.getBagSize(); locationIndex++) {

                //位置和ID相同不行吗
                Item itemTmp = itemMap.get(locationIndex);
                // 如果是用一种物品且堆叠未满
                if (itemTmp != null && itemTmp.getItemInfo().getId().equals(item.getItemInfo().getId())) {
                    itemTmp.setCount(itemTmp.getCount() + item.getCount());
                    notify.notifyPlayer(player,
                            MessageFormat.format("物品{0} x {1}  放入了你的背包\n",
                                    item.getItemInfo().getName(),item.getCount()));
                    return true;
                }*/
        /*    }
        }*/

            // 遍历背包所有格子，如果是空格，将物品放入格子
      /*  for (int locationIndex=1; locationIndex <= bag.getBagSize(); locationIndex++) {
            item.setLocationIndex(locationIndex);
            if (null == bag.getItemMap().putIfAbsent(locationIndex,item)) {
                notify.notifyPlayer(player,
                        MessageFormat.format("物品{0} x {1}  放入了你的背包 \n",
                                item.getItemInfo().getName(),item.getCount()));
                return true;
            }
        }
        return false;*/

        } else {
            if (itemMap.size() < Constant.packageSize) {
                itemMap.put(item.getId(), item);
            } else {
                notify.notifyPlayer(player, "背包已满，无法放入装备");
                flag=false;
            }
        }
        return flag;
    }


        /**
         * 创建物品条目
         */
    public Item createItemByItemInfo(Integer itemId, Integer number) {
        ItemInfo thingInfo = getItemInfo(itemId);

        if (Objects.isNull(thingInfo)) {
            return null;
        }
        return new Item(generateItemId(),number,thingInfo);
    }


    /**
     *  物品条目随机数
     */
    public Long generateItemId() {

        Long resulId=System.currentTimeMillis();
        Random random=new Random();

        StringBuilder stringBuilder=new StringBuilder();

        stringBuilder.append(resulId);
        stringBuilder.append(random.nextInt(10));

        return Long.valueOf(String.valueOf(stringBuilder));


    }

    /**
     *   从数据库加载背包
     */

    public void loadBag(Player player) {


        List<Bag> bagList = bagMapper.selectBagByPlayerId(player.getId());

        bagList.forEach( tBag ->  {

                Bag bag = new Bag(tBag.getPlayerId(),tBag.getBagSize());

                if (!Strings.isNullOrEmpty(tBag.getItems())) {
                    Map<Long,Item> itemMap =  JSON.parseObject(tBag.getItems(),
                            new TypeReference<Map<Long,Item>>(){});
                    bag.setItemMap(itemMap);
                } else {
                    bag.setItemMap(new LinkedHashMap<>());
                }

                bag.setBagName(tBag.getBagName());
                player.setBag(bag);

                log.debug("bag {} ", bag );

        });
    }


    /**
     *  持久化背包数据
     */
    public void saveBag(Player player){

        Bag saveBag=new Bag();
        Bag bag = player.getBag();
        saveBag.setPlayerId(player.getId());

        saveBag.setBagName(bag.getBagName());
        saveBag.setBagSize(bag.getBagSize());
        saveBag.setItems(JSON.toJSONString(bag.getItemMap()));

        if (bagMapper.updateByPrimaryKeySelective(saveBag) > 0) {
            log.debug("更新背包成功 {}",saveBag);


            TODO 将语句写好;

        } else {
            bagMapper.insertBag(saveBag);
            log.debug("保存背包成功 {}",saveBag);
        }
    }





}
