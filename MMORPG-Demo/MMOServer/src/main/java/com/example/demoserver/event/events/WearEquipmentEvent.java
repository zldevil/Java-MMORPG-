package com.example.demoserver.event.events;

import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WearEquipmentEvent {

    Player player;

    ItemInfo itemInfo;

}
