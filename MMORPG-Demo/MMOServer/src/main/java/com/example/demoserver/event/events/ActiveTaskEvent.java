package com.example.demoserver.event.events;

import com.example.demoserver.event.common.GameEvent;
import com.example.demoserver.game.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data

public class ActiveTaskEvent extends GameEvent {
    Player player;
    List<Integer> taskIdList;

    public ActiveTaskEvent(Player player,List<Integer> taskIdList){
        this.player=player;
        this.taskIdList=taskIdList;
    }
}
