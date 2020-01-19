package com.example.demoserver.game.bag.model;

import lombok.Data;


@Data
public class Item {


    //生成算法生成
    private long id;

    //代表数量，所以叠加
    private Integer count; // 武器不能叠加

    private ItemInfo itemInfo;

    //在背包中的位置索引，默认为0
    private Integer locationIndex = 0;


   /* private Long durable = -1L;

    public void setDurable(Long durable) {
        if(durable <= 0) {
            durable = 0L;
        }
        this.durable = durable;
    }*/


    private Integer level;

    public Item() {}


    //装备
    public Item(Long id, Integer count, ItemInfo itemInfo, Integer level) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
        //设置磨损度
        //this.durable = itemInfo.getDurable();
        this.level = level;
    }

    //物品
    public Item(Long id, Integer count, ItemInfo itemInfo) {
        this.id = id;
        this.count = count;
        this.itemInfo = itemInfo;
    }

}
