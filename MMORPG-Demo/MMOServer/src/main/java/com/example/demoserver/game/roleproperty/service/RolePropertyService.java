package com.example.demoserver.game.roleproperty.service;

import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.model.ItemProperty;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.roleproperty.dao.RolePropertyCache;
import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.server.net.utils.SplitParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class RolePropertyService {


    @Resource
    private RolePropertyCache rolePropertyCache;

    @Resource
    private PlayerDataService playerDataService;


    /**
     *  加载物品的增益到玩家属性中
     * @param player 玩家
     * @param itemInfo 物品
     * @return 是否加载属性成功
     */
    public boolean loadThingPropertyToPlayer(Player player, ItemInfo itemInfo) {


        List<ItemProperty> itemPropertyList= SplitParameters.split(itemInfo);

        Map<Integer,RoleProperty> playerPropertyMap = player.getRolePropertyMap();

        itemPropertyList.forEach(itemProperty -> {

            Integer idTmp=itemProperty.getId();

            Long valueOfItem=itemProperty.getValue();

            Long valueResult =playerPropertyMap.get(idTmp).getPropertyValue()+valueOfItem;

            playerPropertyMap.get(idTmp).setPropertyValue(valueResult);

        });

        player.setRolePropertyMap(playerPropertyMap);

        // 计算战力
       // playerDataService.computeAttack(player);
        return true;
    }


    public boolean removeThingPropertyForPlayer(Player player, ItemInfo itemInfo) {

        List<ItemProperty> itemPropertyList= SplitParameters.split(itemInfo);

        Map<Integer,RoleProperty> playerPropertyMap = player.getRolePropertyMap();

        itemPropertyList.forEach(itemProperty -> {

            Integer idTmp=itemProperty.getId();

            Long valueOfItem=itemProperty.getValue();

            Long valueResult =playerPropertyMap.get(idTmp).getPropertyValue()-valueOfItem;

            playerPropertyMap.get(idTmp).setPropertyValue(valueResult);

        });

        player.setRolePropertyMap(playerPropertyMap);

        // 计算战力
        //playerDataService.computeAttack(player);

        return true;
    }




    /**
     *  加载角色属性
     * @param player 角色属性
     */
    public void loadRoleProperty(Player player) {
        Map<Integer,RoleProperty> rolePropertyMap = player.getRolePropertyMap();

        for (int key=1; key <=10; key++ ) {
            RoleProperty roleProperty = rolePropertyCache.get(key);
            // 每个玩家角色的属性都独立
            RoleProperty playerRoleProperty  = new RoleProperty();
            BeanUtils.copyProperties(roleProperty, playerRoleProperty);
            rolePropertyMap.put(roleProperty.getId(),playerRoleProperty);
            //log.debug("rolePropertyMap {}",rolePropertyMap);
        }

        // 加载hp
        Optional.ofNullable(rolePropertyMap.get(1))
                .ifPresent( roleProperty -> player.setHp((long)roleProperty.getPropertyValue()));

        // 加载mp
        Optional.ofNullable(rolePropertyMap.get(2))
                .ifPresent( roleProperty -> player.setMp((long)roleProperty.getPropertyValue()));

    }


    /**
     *  获取属性
     * @param id 属性id
     * @return 可能为空
     */
    public RoleProperty getRoleProperty(Integer id) {
        return rolePropertyCache.get(id);
    }
}
