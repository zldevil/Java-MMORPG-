package com.example.demoserver.game.ScenceEntity.model;


//打算让他充当标识接口，起到分类的作用
public interface Characters {


    /*//角色id
    Long getId();

    //角色名
    String getName();

    //角色血量
    Long getHp();
    void setHp(Long hp);

    //角色mp
    Long getMp();
    void setMp(Long mp);

    //角色状态，生存 or 死亡
    Integer getState();
    void setState(Integer state);

   *//** //角色拥有的技能
    Map<Integer, Skill> getSkillInUsedMap();
    void setSkillInUsedMap(Map<Integer, Skill> skillMap);


    //正在作用的技能
    Map<Skill, Future> getSkillInEffectingMap();
    void setSkillInEffectingMap(Map<Skill, Future> skillMap);

    *//**//** 角色的当前作用和即将作用的buff*//**//*
    List<Buff> getBufferList();
    void setBufferList(List<Buff> bufferList);*//*


    //角色的场景id
    Integer getSenceId();
    void setSenceId(Integer senceId);*/
}
