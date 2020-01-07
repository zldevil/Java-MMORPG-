package com.example.demoserver.game.buff.model;


import com.example.demoserver.common.commons.Character;

public class Buff {

    private Integer id;

    private  String name;

    private  Integer type;

    private  Long hpRecover;

    private Long mpRecover;
    //效果
    private Integer effect;

    //持续时间
    private Integer duration;

    //时间间隔指的是什么？？？？
    private Integer intervalTime;

    //作用于玩家角色
    //private Character character;




   /* public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }*/

  /*  public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getHpRecover() {
        return hpRecover;
    }

    public void setHpRecover(Long hpRecover) {
        this.hpRecover = hpRecover;
    }

    public Long getMpRecover() {
        return mpRecover;
    }

    public void setMpRecover(Long mpRecover) {
        this.mpRecover = mpRecover;
    }

    public Integer getEffect() {
        return effect;
    }

    public void setEffect(Integer effect) {
        this.effect = effect;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }



}
