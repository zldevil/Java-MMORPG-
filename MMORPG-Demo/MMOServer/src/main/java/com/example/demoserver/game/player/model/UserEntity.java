package com.example.demoserver.game.player.model;

import lombok.Data;

/**
 *场景角色单位
 */
@Data
public class UserEntity {


    /**
     * 玩家ID
     */
    private Integer Id;

    /**
     * 玩家昵称
     */
    private String name;

    /**
     * 从属用户
     */
    private Integer userId;

    /**
     * 角色状态
     */
    private Integer state;

    /**
     * 角色类型,职业
     */
    private Integer typeId;

     /**
     * 从属场景Id
     */
    private Integer senceId;

    /**
     * 角色经验值
     * */
    private Integer exp;

    /**
     * 装备名称
     * */
    private String equipments;

    /** 代表朋友的JSON串**/
    private String friends;

    /** 代表玩家所拥有的金钱**/
    private Integer money;

    /** 代表玩家加入的公会ID**/
    private Integer guildId;

    /** 代表玩家在公会中的职位**/
    private Integer positionInGuild;




    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getEquipments() {
        return equipments;
    }

    public void setEquipments(String equipments) {
        this.equipments = equipments;
    }

}
