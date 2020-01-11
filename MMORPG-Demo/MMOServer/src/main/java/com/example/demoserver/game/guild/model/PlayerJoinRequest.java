package com.example.demoserver.game.guild.model;


import com.example.demoserver.game.player.model.Player;
import lombok.Data;

import java.util.Date;


@Data
public class PlayerJoinRequest {


    public PlayerJoinRequest(boolean isAgree, Date date, Player player) {
        this.isAgree = isAgree;
        this.date = date;
        this.player = player;
    }


    private boolean isAgree;

    private Date date;

    Player player;
}
