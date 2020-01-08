package com.example.demoserver.game.buff.model;



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


    private Integer intervalTime;

    private Long useBuffTime;



    public Long getUseBuffTime() {
        return useBuffTime;
    }

    public void setUseBuffTime(Long useBuffTime) {
        this.useBuffTime = useBuffTime;
    }

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
