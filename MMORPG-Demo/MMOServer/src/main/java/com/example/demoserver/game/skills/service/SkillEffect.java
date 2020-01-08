package com.example.demoserver.game.skills.service;

import com.example.demoserver.common.commons.Character;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.buff.service.BuffService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.server.notify.Notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Description: 技能作用
 */
@Component
@Slf4j
public class SkillEffect {



    @Resource
    private BuffService buffService;

    @Resource
    private Notify notify;


    //private  Map<Integer, ISkillEffect>  skillEffectMap = new HashMap<>();


    /**
     *  根据技能类型触发技能效果
     * @param skillTypeId 技能类型id
     * @param initiator 施放者
     * @param target 施放目标
     * @param gameScene 场景
     * @param skill 技能
     */

   /* public void castSkill(Integer skillTypeId,
                          Character initiator,
                          Character target,
                          GameScene gameScene,
                          Skill skill) {
        Optional .ofNullable(skillEffectMap.get(skillTypeId)).ifPresent(s -> s.skillEffect(initiator,target,gameScene,skill));
    }
*/

    {
      /*  skillEffectMap.put(SkillType.ATTACK_SINGLE.getTypeId(),this::attackSingle);
        skillEffectMap.put(SkillType.CALL_PET.getTypeId(),this::callPet);
        skillEffectMap.put(SkillType.TAUNT.getTypeId(),this::taunt);
        skillEffectMap.put(SkillType.FRIENDLY.getTypeId(),this::friendly);
        // 群体攻击技能
        skillEffectMap.put(SkillType.ATTACK_MULTI.getTypeId(),this::attackSMulti);
*/
    }

    /**
     *  群体攻击技能
     */
    private void attackSMulti(Character initiator, Character target, GameScene gameScene, Skill skill) {
      /*  // 消耗mp和损伤目标hp
        initiator.setMp(initiator.getMp() - skill.getMpConsumption());
        target.setHp(target.getHp() - skill.getHurt());
        target.setHp(target.getHp() + skill.getHeal());
        notify.notifyScene(gameScene,
                MessageFormat.format(" {0} 受到 {1} 群体攻击技能 {2}攻击，  hp减少{3},当前hp为 {4}\n",
                        target.getName(),initiator.getName(),skill.getName(),skill.getHurt(), target.getHp()));
*/
    }



    /**
     *  施放单体攻击技能造成影响
     * @param initiator 施放者
     * @param target 施放目标
     * @param gameScene 场景
     * @param skill 技能
     */
    private  void attackSingle(Character initiator, Character target, GameScene gameScene, Skill skill) {
        // 消耗mp和损伤目标hp
        initiator.setMp(initiator.getMp() - skill.getConsumeMp());

        target.setHp(target.getHp() - skill.getHarmValue());

        // 如果技能触发的buffer不是0，则对敌方单体目标释放buffer
        if (!skill.getBuff().equals(0)) {
            Buff buffer = buffService.getBuff(skill.getBuff());
            // 如果buffer存在则启动buffer
            Optional.ofNullable(buffer).map(
                    (b) -> {
                        try {
                            return buffService.startBuffer(target,b);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
            );
        }

        notify.notifyScene(gameScene,
                MessageFormat.format(" {0} 受到 {1} 技能 {2}攻击，  hp减少{3},当前hp为 {4}\n",
                        target.getName(),initiator.getName(),skill.getName(),skill.getHarmValue(), target.getHp()));
    }


    /**
     *  嘲讽技能
     */
    private void taunt(Character initiator, Character target, GameScene gameScene, Skill skill) {

        /*// 将怪物目标设定为发起者
        target.setTarget(initiator);

        // 消耗mp和损伤目标hp
        initiator.setMp(initiator.getMp() - skill.getMpConsumption());
        target.setHp(target.getHp() - skill.getHurt());
        target.setHp(target.getHp() + skill.getHeal());

        notify.notifyScene(gameScene,
                MessageFormat.format(" {0} 受到 {1} 技能 {2}攻击，  hp减少{3},当前hp为 {4}， {0}受到嘲讽\n",
                        target.getName(),initiator.getName(),skill.getName(),skill.getHurt(), target.getHp()));*/

    }


    /**
     *  对友方使用的技能
     */
   private void friendly(Character initiator, Character target, GameScene gameScene, Skill skill) {
        // 消耗mp和治疗目标hp
       /* initiator.setMp(initiator.getMp() - skill.getConsumeMp());
        target.setHp(target.getHp() + skill.getHeal());

        if(skill.getHeal() > 0) {
            notify.notifyScene(gameScene, MessageFormat.format("{0} 受到 {1} 的治疗,hp增加了 {2}，当前hp是 {3}",
                    target.getName(),initiator.getName(),skill.getHeal(),target.getHp()));
        }*/

    }









}
