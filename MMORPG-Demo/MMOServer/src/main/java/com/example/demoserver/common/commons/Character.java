package com.example.demoserver.common.commons;


import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.skills.model.Skill;

import java.util.List;
import java.util.Map;

/**
 * 场景上的一个单位，规范角色的必有的属性方法
 */

public interface Character {


   /*
    //角色的场景id
    Integer getSenceId();

    void setSenceId(Integer characterSenceId);*/

    /** 活物的id */
    Integer getId();

    /** 活物的名字 **/
    String getName();

    /** 活物的血量 */
    Long getHp();
    void setHp(Long hp);

    /** 活物的mp */
    Long getMp();
    void setMp(Long hp);


    /** 活物的状态，生存 or 死亡 */
    Integer getState();
    void setState(Integer state);


    /**  活物当前使用的技能 */
    Map<Integer, Skill> getHasUseSkillMap();
    void setHasUseSkillMap(Map<Integer, Skill> skillMap);

    /** 活物的当前buffer */
    List<Buff> getBufferList();
    void setBufferList(List<Buff> bufferList);


    /** 活物的当前目标 */
    Character getTarget();
    void setTarget(Character target);


}
