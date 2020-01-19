package com.example.demoserver.game.gamecopy.model;

import com.example.demoserver.game.SceneEntity.model.Monster;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@AllArgsConstructor
public class CommonMonsterInCopy {

    private Integer senceEntityId;

    private Integer num;

   // List<Monster> monsterList=new CopyOnWriteArrayList<>();



}
