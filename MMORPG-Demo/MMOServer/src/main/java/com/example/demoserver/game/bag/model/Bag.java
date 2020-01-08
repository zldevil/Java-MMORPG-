package com.example.demoserver.game.bag.model;

import com.alibaba.fastjson.*;

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

    private String items;


    JSONObject json = JSONObject.parseObject(items);

    Map<Long,Item> itemMap =new ConcurrentSkipListMap<>();


    /**
     *
     * @param playerId
     * @param bagName
     * @param bagSize
     */
    public Bag(Integer playerId, String bagName, Integer bagSize) {
        this.playerId = playerId;
        this.bagName = bagName;
        this.bagSize = bagSize;

    }

    /**
     *
     * @param playerId
     * @param bagSize
     */
    public Bag(Integer playerId, Integer bagSize) {
        this.playerId = playerId;
        this.bagSize = bagSize;
    }






}
