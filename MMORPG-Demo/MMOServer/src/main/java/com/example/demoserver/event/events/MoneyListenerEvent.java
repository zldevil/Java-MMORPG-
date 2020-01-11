package com.example.demoserver.event.events;

import com.example.demoserver.event.common.GameEvent;
import com.example.demoserver.game.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyListenerEvent  extends GameEvent {
    Player player;
    Integer money;
}
