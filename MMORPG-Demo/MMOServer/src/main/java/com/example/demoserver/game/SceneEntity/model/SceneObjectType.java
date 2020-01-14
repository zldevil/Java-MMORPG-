package com.example.demoserver.game.SceneEntity.model;

/**

 * Description: 场景对象的类型
 */
public enum  SceneObjectType {

    /** npc类型*/
    NPC(1),
    /** 野怪类型 */
    WILD_MONSTER(2);


    Integer type;


    SceneObjectType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

}
