package com.example.demoserver.game.bag.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Data
@Slf4j
public class Bag  {


    private Integer playerId;

    private String bagName;

    private Integer bagSize;

    //啥意思？，背包类型是是啥意思
    private Integer type;


    //时间复杂度为O（1），每次取到物品
    Map<Integer,Item> itemMap = new ConcurrentSkipListMap<>();


    public Bag(Integer playerId, String bagName, Integer bagSize, Integer type) {
        this.playerId = playerId;
        this.bagName = bagName;
        this.bagSize = bagSize;
        this.type = type;

    }

    public Bag(Integer playerId, Integer bagSize) {
        this.playerId = playerId;
        this.bagSize = bagSize;
    }






}
