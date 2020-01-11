package com.example.demoserver.game.player.model;

import lombok.Data;

/**
 *场景角色单位
 */
@Data
public class UserEntity {


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSenceId() {
        return senceId;
    }

    public void setSenceId(Integer senceId) {
        this.senceId = senceId;
    }

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
    //角色属于哪个用户
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


    private Integer money;

    //加入的公会的id
    private Integer guildId;

    //在工会的位置
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
