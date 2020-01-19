package com.example.demoserver.game.team.model;

import com.example.demoserver.game.player.model.Player;
import lombok.Data;

import java.util.Map;



@Data
public class Team {


    private Integer id;

    /** 队长id */
    private Integer captainId;

    private Map<Integer, Player> teamPlayer ;

    /** 小队默认是四人一队 */
    private Integer teamSize = 4;


    public Team(Integer id, Map<Integer, Player> teamPlayer) {
        this.id = id;
        this.teamPlayer = teamPlayer;
    }

}
