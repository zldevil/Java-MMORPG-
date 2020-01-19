package com.example.demoserver.game.SceneEntity.model;


import com.example.demoserver.common.commons.Character;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.skills.model.Skill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
  * Description: 怪物
 */


@Data
@EqualsAndHashCode(callSuper=true)
@ToString(exclude = {"target"})
public class Monster extends ScenceEntity  implements Character   {


    /** 当前攻击目标 */
    Character targetCharacter;

    private long attackTime = System.currentTimeMillis();

    public String displayData() {
        return MessageFormat.format("id:{0}  name:{1}  hp:{2}  mp:{3}  {4}"
                ,this.getId(),this.getName(), this.getHp(), this.getMp(), this.getState()==-1?"死亡":"存活" );
    }

    @Override
    public Map<Integer, Skill> getHasUseSkillMap() {
        return null;
    }

    @Override
    public void setHasUseSkillMap(Map<Integer, Skill> skillMap) {

    }

    @Override
    public List<Buff> getBufferList() {
        return null;
    }

    @Override
    public void setBufferList(List<Buff> bufferList) {

    }

    @Override
    public Character getTarget() {
        return null;
    }

    @Override
    public void setTarget(Character target) {

    }
}
