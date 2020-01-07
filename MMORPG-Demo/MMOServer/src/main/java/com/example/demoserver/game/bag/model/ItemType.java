package com.example.demoserver.game.bag.model;


public enum ItemType {


    //装备
    EQUIT_ITEM(1),
    //可叠加消耗品
    CONSUMABLE_ITEM(2),
    //消耗品
    CAN_BE_STACKED(3);

    private Integer type;


    ItemType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }


}
