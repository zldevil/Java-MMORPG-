package com.example.demoserver.game.skills.service;

import com.example.demoserver.common.commons.Character;
import com.example.demoserver.common.commons.Constant;
import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.game.ScenceEntity.model.Monster;
import com.example.demoserver.game.ScenceEntity.service.MonsterService;
import com.example.demoserver.game.buff.dao.BuffCache;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.buff.service.BuffService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.game.skills.dao.SkillsCache;
import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.game.skills.model.SkillType;
import com.example.demoserver.server.notify.Notify;
import com.example.demoserver.timejob.TimeTaskThreadManager;
import com.google.common.eventbus.EventBus;
import com.sun.xml.internal.bind.v2.TODO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkillService {


    @Resource
    private BuffService buffService;

    @Resource
    private Notify notify;

    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private GameSceneService gameSceneService;

    @Autowired
    private SkillsCache skillsCache;

    @Autowired
    private MonsterService monsterService;


    /**
     *  使用技能攻击怪物
     * @param ctx 上下文
     * @param skillId 技能id
     * @param targetIdList 目标id列表
     */
    public void useSkillAttackMonster(ChannelHandlerContext ctx, Integer skillId,  List<Integer> targetIdList ) {

        Player player = playerDataService.getPlayerByCtx(ctx);
        Skill skill = skillsCache.get(skillId);

        GameScene gameScene = gameSceneService.getSceneByPlayer(player);

        if(System.currentTimeMillis()-player.getHasUseSkillMap().get(skillId).getUseSkillTime()>player.getHasUseSkillMap().get(skillId).getCd()){

            notify.notifyPlayer(player,"技能冷却中");
            return ;
        }

        //不只是武器拥有耐久度，在使用技能时所有装备均消耗耐久度
        player.getEquipmentBar().values().forEach(item -> {
            if(item.getDurable()-3<0){
                notify.notifyPlayer(player,MessageFormat.format("{0}的耐久度不足",item.getItemInfo().getName()));
            }
        });

        if (!skill.getType().equals(SkillType.ATTACK_MULTI.getTypeId())) {
            notify.notifyPlayer(player,"该技能不能对多个目标使用");

        }

        //PVE,玩家对战环境中怪物等
        if (targetIdList.size() >1) {
            targetIdList.forEach(
                    targetId -> skillPVE(player,targetId,gameScene,skill)
            );
        } else {
            skillPVE(player,targetIdList.get(0),gameScene,skill);
        }
    }

    /**
     *  PVE
     * @param player 玩家
     * @param targetId id
     * @param gameScene 场景
     * @param skill 技能
     */
    private void skillPVE(Player player, Integer targetId, GameScene gameScene, Skill skill){

        Monster target = gameScene.getMonsters().get(targetId);

        if (null == target) {
            notify.notifyPlayer(player,"目标怪物不存在此场景");
            log.info("怪物不存在");
            return;
        }

        player.setTarget(target);
        // 使用技能
        if (useSkill(player,target,gameScene,skill)) {

            //瞬发技能
            monsterService.monsterBeAttack(player,target);
            notify.notifyScene(gameScene,
                    MessageFormat.format("{0} 受到 {1} 的技能 {2} 攻击，hp减少{3},当前hp为 {4} \n"
                            ,target.getName(),player.getName(),skill.getName(),skill.getHarmValue(), target.getHp()));


        }else {
            notify.notifyPlayer(player,"使用技能攻击怪物失败，可能因为MP不足");
        }
    }

    /**
     *   活物使用技能
     * @param character 技能发起者
     * @param target 技能目标
     * @param skill 技能
     * @return 是否成功
     */

    public boolean useSkill(Character character,Character target,GameScene gameScene,Skill skill){

        if(character.getMp()>=skill.getConsumeMp()) {
            log.debug("开始使用技能");
            //瞬发技能，
            notify.notifyScene(gameScene,
                    MessageFormat.format(" {0}  对 {1} 使用了技能  {2} ",
                            character.getName(), target.getName(), skill.getName()));

            character.setMp(character.getMp() - skill.getConsumeMp());
            Long finalSkillHarmValue=null;
            if(character.getClass().equals(Player.class)){

                Player player=(Player)character;

                finalSkillHarmValue=computeFinalHarmValue((List<RoleProperty>) player.getRolePropertyMap().values(),skill);

            } else {
                finalSkillHarmValue=skill.getHarmValue();
            }
            if (finalSkillHarmValue>= target.getHp()) {

                // 怪物死亡,通知场景玩家;
                notify.notifyScene(gameScene, MessageFormat.format("{0} 被 {1} 杀死", target.getName(), character.getName()));
            }

            target.setHp(target.getHp() - skill.getHarmValue());

            if (skill.getBuff().equals(0)) {

                Buff buff = buffService.getBuff(skill.getBuff());

                try {
                    buffService.startBuffer(target, buff);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            /**
             * 玩家能看到怪物状态，所以通知所有玩家
             */
            notify.notifyScene(gameScene, MessageFormat.format("怪物 {0}受到技能攻击，减少了 {1} HP,怪物当前HP为{2}",
                    target.getName(), skill.getHarmValue(), target.getHp()));

            // skillEffect.castSkill(skill.getType(),character,target,gameScene,skill);

            /**
             * 技能存在冷却
             */

            startSkillCd(character, skill);

            return true;
        }

        /**
         * 这个方法位置不确定，持续恢复Mp。直到角色MP到达最大值
         */
        try {
            ScheduledFuture<?> futureTask=TimeTaskThreadManager.scheduleAtFixedRate(0,2000,()->{
                character.setMp(character.getMp()+10);
            }
            );
            if(character.getMp()>= Constant.MAXMP){
                futureTask.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     *  开始技能
     * @param character 活物
     * @param skill 技能
     */

    //已经使用的技能放进一个MAP表中
    public void startSkillCd(Character character, Skill skill) {

        Skill playerSkill = new Skill();
        BeanUtils.copyProperties(skill,playerSkill);

        character.getHasUseSkillMap().put(skill.getId(),skill);

        // 技能cd结束后，移出活物cd状态
        playerSkill.setUseSkillTime(System.currentTimeMillis());

        try {
            TimeTaskThreadManager.threadPoolSchedule(skill.getCd(), () ->character.getHasUseSkillMap().remove(skill.getId()) );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 计算伤害
     * @param propertyList
     * @param skill
     * @return
     */
    public Long computeFinalHarmValue( List<RoleProperty> propertyList,Skill skill){

        //生成一个属性名-属性数值的map
        Map<String,Long> map=propertyList.stream().collect(
                Collectors.toMap(RoleProperty::getPropertyName,RoleProperty::getPropertyValue)
        );

        Long finalValue=(long)(map.get("攻击力")+map.get("力量")*10*skill.getSkillDamagePercent());
        return finalValue;
    }
    


}
