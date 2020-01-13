package com.example.demoserver.game.equip.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.model.ItemType;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.equip.model.EquitmentPart;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.roleproperty.service.RolePropertyService;
import com.example.demoserver.server.notify.Notify;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class EquipmentBarService {


    @Autowired
    private BagService bagsService;

    @Autowired
    private RolePropertyService rolePropertyService;

    @Autowired
    private  Notify notify;


    /**
     * 穿上装备
     *
     * @param player 玩家
     *
     * @return 成功或失败
     */

    public boolean wearEquip(Player player, Long itemId) {

        Map<String, Item> equipmentBar = player.getEquipmentBar();

        Item item = player.getBag().getItemMap().get(itemId);
        if (null == item) {
            notify.notifyPlayer(player,"背包中没有此装备");
            return false;
        }
        ItemInfo itemInfo = item.getItemInfo();

        // 判断是否是装备,装备的种类代号为
        if (null == itemInfo || !itemInfo.getType().equals(ItemType.EQUIT_ITEM.getType())) {
            return false;
        }

        Item preItem = equipmentBar.get(itemInfo.getLocation());
        if (null != preItem) {
            // 如果原来这给部位有装备，移除原先装备的属性
            removeEquip(player,preItem.getItemInfo().getLocationDescription());

        }

        // 穿上装备，改变玩家属性
        rolePropertyService.loadThingPropertyToPlayer(player, itemInfo);

        // 放入玩家的装备栏中
        player.getEquipmentBar().put(itemInfo.getLocationDescription(), item);

        // 从背包移除物品
        bagsService.removeItem(player,itemId,1);


        // 将原先的物品放入背包
        bagsService.addItem(player, preItem);

        return true;
    }


    /**
     *      卸下装备
     * @param player 玩家
     * @param part 需要卸下装备的部位
     * @return 是否卸下装备
     */

    public void removeEquip(Player player, String part)  {

        Map<String,Item> equipmentBar = player.getEquipmentBar();

        Item item = equipmentBar.get(part);

        if (item == null) {
           notify.notifyPlayer(player,"此装备未穿戴");
        }

        // 移除属性增益,放入背包
        if ( equipmentBar.remove(part,item)
                &&bagsService.addItem(player,item)
                && rolePropertyService.removeThingPropertyForPlayer(player,item.getItemInfo())) {
            notify.notifyPlayer(player,"卸载装备成功，并放回背包");
        } else {
            notify.notifyPlayer(player,"卸载装备失败，背包已满");
        }

    }

    /**
     * 加载装备
     */
    public void loadEquipment(Player player) {
        String equipmentBarString = player.getEquipments();

        if (!Strings.isNullOrEmpty(equipmentBarString)) {

            Map<String, Item> equipmentBarMap = JSON.parseObject(equipmentBarString,
                    new TypeReference<Map<String, Item>>() {
                    });
            log.debug("  equipmentBarString {}", equipmentBarString);
            log.debug("  equipmentBar{}", equipmentBarMap);

            player.setEquipmentBar(equipmentBarMap);

            equipmentBarMap.values()
                    .forEach(itemEquipment ->
                            // 改变玩家属性
                            rolePropertyService.loadThingPropertyToPlayer(player, itemEquipment.getItemInfo()));
        }

    }

    public void showEquip(Player player){
        if (Objects.isNull(player)) {
            notify.notifyPlayer(player,"角色未登录");
            return;
        }

        Map<String, Item> equipmentBar = player.getEquipmentBar();

        if (equipmentBar.isEmpty()) {
            notify.notifyPlayer(player,"您还没有装备");
        }
        StringBuilder sb = new StringBuilder();
        equipmentBar.values().stream().
                map(Item::getItemInfo).
                forEach(
                        itemInfo -> {
                            String locationName= EquitmentPart.getPartByCode(itemInfo.getLocation());
                            sb.append(MessageFormat.format(" {0} 上的装备为 {1} ",
                                    locationName, itemInfo.getName())
                            );

                            sb.append("\n");
                        }
                );


        notify.notifyPlayer(player,sb);
    }
}