package com.example.demoserver.game.SceneEntity.model;

import lombok.Data;

@Data
public class ScenceEntity {


    private Integer id;

    private String name;

    private Long hp;

    private Long mp;

    private Long attack;

    private String talk = "";

    private Integer state;

    private Integer roleType;


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

    public Long getHp() {
        return hp;
    }

    public void setHp(Long hp) {
        this.hp = hp;
    }

    public Long getMp() {
        return mp;
    }

    public void setMp(Long mp) {
        this.mp = mp;
    }

    public Long getAttack() {
        return attack;
    }

    public void setAttack(Long attack) {
        this.attack = attack;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    //设置死亡时间，怪物被杀死后定时重生
    private long deadTime;



    /**
     * // 当前使用技能的集合
    Map<Integer, Skill> hasUseSkillMap = new ConcurrentHashMap<>();


    // 当前攻击对象
   // private Characters target;




    /*public  Long getDeadTime() {
        return deadTime;
    }
    public  void  setDeadTime(Long deadTime){
        this.deadTime = deadTime;
    }
*/

    public Integer getKey() {
        return id;
    }
}
