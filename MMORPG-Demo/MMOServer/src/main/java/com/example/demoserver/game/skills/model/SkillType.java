package com.example.demoserver.game.skills.model;


public enum SkillType {


    /** 对自身或友方使用 */
    FRIENDLY(2),

    /** 对敌人单人使用 **/
    ATTACK_SINGLE(1),

    /** 多人技能 **/
    ATTACK_MULTI(3),

    /** 只能自身使用 */
    ONLY_SELF(6),

    /** 嘲讽技能 **/
    TAUNT(7);

    SkillType(Integer typeId) {
        this.typeId = typeId;
    }

    private Integer typeId;


    public Integer getTypeId() {
        return typeId;
    }

}
