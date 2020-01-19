package com.example.demoserver.game.SceneEntity.model;


import lombok.Data;

@Data
public class Drop {


    // 掉落概率
    double probability;

    // 掉落物品
    Integer itemId;

}
