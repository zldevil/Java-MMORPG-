package com.example.demoserver.game.bag.model;

import com.example.demoserver.game.roleproperty.model.RoleProperty;
import lombok.Data;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Data
public class ItemInfo {


    private Integer id;

    private String name;

    private Integer level;

    private Integer location;

    //物品触发效果
    private Integer buff;

    private Integer price;

    /**
     * jsonstr
     */
    private  String itemInfoProperty;

    private String describe;

    //武器耐久度
    private Long durable;


    private Integer repairPrice;

    private  Integer type;

    /** 物品属性 */
    private Map<Integer,RoleProperty> itemRoleProperty = new HashMap<>();


}
