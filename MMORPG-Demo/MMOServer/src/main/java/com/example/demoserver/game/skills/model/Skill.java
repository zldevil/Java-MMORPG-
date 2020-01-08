package com.example.demoserver.game.skills.model;

import lombok.Data;

@Data
public class Skill {

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

    public Long getConsumeMp() {
        return consumeMp;
    }

    public void setConsumeMp(Long consumeMp) {
        this.consumeMp = consumeMp;
    }

    public Long getHarmValue() {
        return harmValue;
    }

    public void setHarmValue(Long harmValue) {
        this.harmValue = harmValue;
    }

    public Integer getCd() {
        return cd;
    }

    public void setCd(Integer cd) {
        this.cd = cd;
    }

    private Integer id;
    private String name;
    private Long consumeMp;
    private Long harmValue;
    private Integer cd;
    private  Integer type;
    private Integer buff;

    private Float skillDamagePercent;

    private Long useSkillTime;

    /**
     * 技能等级，技能施放时间。。。
     */
}
