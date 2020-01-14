package com.example.demoserver.game.scene.model;


import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.SceneEntity.model.NPC;
import com.example.demoserver.game.player.model.Player;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**

 */


@Data
@Component
public class GameScene  {


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighborScence() {
        return neighborScence;
    }

    public void setNeighborScence(String neighborScence) {
        this.neighborScence = neighborScence;
    }

    public String getScenceEntityId() {
        return scenceEntityId;
    }

    public void setScenceEntityId(String scenceEntityId) {
        this.scenceEntityId = scenceEntityId;
    }



    private Integer id;

    private String name;

    private String neighborScence ;

    private String scenceEntityId;


    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public Map<Integer, NPC> getNpcs() {
        return npcs;
    }

    public Map<Integer, Monster> getMonsters() {
        return monsters;
    }

    /** 处于场景的玩家,key为player_id */
    private Map<Integer, Player> players = new ConcurrentHashMap<>();


    /** 处于场景中的NPC */
    private  Map<Integer, NPC> npcs = new ConcurrentHashMap<>();

    /** 处于场景中的怪物 */
    private Map<Integer, Monster> monsters = new ConcurrentHashMap<>();


    public String display() {
        return MessageFormat.format("id:{0}  name:{1}"
                ,this.getId(),this.getName());
    }




    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("scene-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 通过一个场景一个线程处理器的方法保证每个场景的指令循序 */
    ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);


}
