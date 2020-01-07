package com.example.demoserver.event.events;

import com.example.demoserver.event.common.GameEvent;
import com.example.demoserver.game.ScenceEntity.model.Monster;
import com.example.demoserver.game.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MonsterDeadEvent extends GameEvent {

    private Player attacter;
    private Monster targetMonster;

}
