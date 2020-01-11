package com.example.demoserver.game.guild.model;

import com.example.demoserver.game.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
@AllArgsConstructor
public class Guild {

    private Integer id;
    private String name;
    private Integer level;

    private String member;

    Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    Map<Integer,PlayerJoinRequest> playerJoinRequestMap =new ConcurrentHashMap<>();

    public Guild(Integer id,String name,Integer level){
        this.id=id;
        this.name=name;
        this.level=level;
    }
}
