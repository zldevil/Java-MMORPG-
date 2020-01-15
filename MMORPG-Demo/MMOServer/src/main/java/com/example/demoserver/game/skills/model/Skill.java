package com.example.demoserver.game.skills.model;

import lombok.Data;

@Data
public class Skill {


    private Integer id;

    private String name;

    private Long consumeMp;

    private Long harmValue;

    private Integer cd;

    private  Integer type;

    private Integer buff;

    private Float skillDamagePercent;

    /**
     * 技能等级，技能施放时间
     */
    private Long useSkillTime;


}
