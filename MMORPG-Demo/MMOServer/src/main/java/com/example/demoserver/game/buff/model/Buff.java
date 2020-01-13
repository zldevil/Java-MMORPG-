package com.example.demoserver.game.buff.model;


import lombok.Data;

@Data
public class Buff {


    private Integer id;

    private  String name;

    private  Integer type;

    private  Long mpRecover;

    private Long hpRecover;
    //效果
    private Integer effect;

    //持续时间
    private Integer duration;

    private Integer intervalTime;

    //在哪个时间点使用的buff
    private Long useBuffTime;


    public Long getUseBuffTime() {
        return useBuffTime;
    }

    public void setUseBuffTime(Long useBuffTime) {
        this.useBuffTime = useBuffTime;
    }



}
