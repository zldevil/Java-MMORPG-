package com.example.demoserver.game.ScenceEntity.model;




import com.example.demoserver.common.commons.Character;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.skills.model.Skill;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**

 * Description: NPC
 */

@Data
@EqualsAndHashCode(callSuper = true)

public class NPC  extends ScenceEntity implements Character {



    public String displayData() {
        return MessageFormat.format("id:{0}  name:{1}  hp:{2}  mp:{3}  {4}"
                ,this.getId(),this.getName(), this.getHp(), this.getMp(), this.getState()==-1?"死亡":"存活");
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
