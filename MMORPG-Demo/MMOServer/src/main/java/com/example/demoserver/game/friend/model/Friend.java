package com.example.demoserver.game.friend.model;

import lombok.Data;

import java.util.Date;

@Data
public class Friend {

    private Integer playerId;

    private String name;

    private String roleClass;

    private Date lastOnlineTime;

    private boolean isOnline;


    public Friend(Integer playerId, String name, String roleClass, Date lastOnlineTime) {
        this.playerId = playerId;
        this.name = name;
        this.roleClass = roleClass;
        this.lastOnlineTime = lastOnlineTime;
    }

}
