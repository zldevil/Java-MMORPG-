package com.example.demoserver.game.SceneEntity.service;

import com.example.demoserver.common.commons.Character;
import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.event.events.MonsterDeadEvent;
import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.gamecopy.cache.GameCopyCache;
import com.example.demoserver.game.gamecopy.model.GameCopy;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scene.model.GameScene;
import com.example.demoserver.game.skills.service.SkillService;
import com.example.demoserver.server.notify.Notify;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

@Service
@Slf4j
public class MonsterService {


    @Resource
    private Notify notify;

    @Resource
    private SkillService skillService;

    @Resource
    private GameObjectService gameObjectService;

    @Resource
    private MonsterDropItemService monsterDropsService;

    @Resource
    private PlayerDataService playerDataService;

    @Autowired
    private GameCopyCache gameCopyCache;

    /**
     * @param character
     * @param monster
     */
    public void monsterBeAttack(Character character, Monster monster) {

        // 将怪物当前目标设置为玩家,让怪物攻击玩家
        if (Objects.isNull(monster.getTargetCharacter())) {
            monster.setTargetCharacter(character);
        }

        Player player = null;
        if (character instanceof Player) {
            player = (Player) character;
        }

       /* if (character instanceof Pet && ((Pet) creature).getMaster() instanceof Player) {
            Pet pet = (Pet) creature;
            player =  (Player) pet.getMaster();
        }
*/
        if (Objects.nonNull(player)) {
            // 如果怪物死亡
            if(monster.getHp()<=0){
                if (gameObjectService.sceneObjectAfterDead(monster)) {
                    // 结算掉落，这里暂时直接放到背包里
                    monsterDropsService.dropItem(player, monster);
                    // 怪物死亡的事件
                    EventManager.publish(new MonsterDeadEvent(player, monster));
                }
            }

        }
    }


    /**
     *  怪物（包括玩家宠物）自动攻击,副本中的自动攻击
     * @param monster   怪物
     * @param gameScene 战斗的场景
     */
    public void startAIAttackInScene(Monster monster, GameScene gameScene) {

        Character target = monster.getTarget();

        if (target instanceof Player){
            // 玩家不在场景内，不进行攻击
            if (null == gameScene.getPlayers().get(target.getId())) {
                monster.setTarget(null);
                return;
            }

            if (playerDataService.isPlayerDead((Player) target, monster)) {
                monster.setTarget(null);
                return;
            }
        }

        if (target.getHp() <=0 || target.getState() == -1) {
            monster.setTarget(null);
            return;
        }

        // 怪物死亡了，不进行攻击
        if (monster.getHp() <=0 || monster.getState() == -1) {
            monster.setTarget(null);
            return;
        }


        monsterAttackPlayer(monster,target);

       /* if ((monster.getAttackTime() + monster.getAttackSpeed()) < System.currentTimeMillis()) {
            // 进行普通攻击
            monsterAttack(monster, target,gameScene);

            // 更新普通攻击的攻击时间
            monster.setAttackTime(System.currentTimeMillis());
        }*/
        // 使用没有冷却的技能
        /*if (monster.getHasUseSkillMap().size() < 1) {
            monsterUseSkill(monster, target,gameScene);
        }*/
    }


    public void startAiAttackInCopy(Monster monster, GameCopy gameCopy){
        Character target = monster.getTarget();

        if (target instanceof Player){
            // 玩家不在场景内，不进行攻击
            if (null == gameCopy.getPlayers().get(target.getId())) {
                monster.setTarget(null);
                return;
            }

            if (playerDataService.isPlayerDead((Player) target, monster)) {
                monster.setTarget(null);
                return;
            }
        }

        if (target.getHp() <=0 || target.getState() == -1) {
            monster.setTarget(null);
            return;
        }

        // 怪物死亡了，不进行攻击
        if (monster.getHp() <=0 || monster.getState() == -1) {
            monster.setTarget(null);
            return;
        }

        monsterAttackPlayer(monster,target);

    }


    /**
     * 场景中的自动攻击
     * @param monster
     * @param character
     */
    public void monsterAttackPlayer(Monster monster,Character character){

        Player player=(Player) character;

        Long monsterHarmValue=monster.getAttack();

        player.setHp(player.getHp()-monsterHarmValue);

        if (player.getGameCopyId()!=0){
           gameCopyCache.get(player.getGameCopyId()).getPlayerMap().values().forEach(playerTmp -> {

               notify.notifyPlayer(playerTmp,MessageFormat.format("怪物{0}攻击玩家,玩家{1} HP减少{2}",
                       monster.getName(),player.getName(),monsterHarmValue
               ));
           });
        }

        //别的玩家能看到
        notify.notifyScene(player.getCurrentScene(), MessageFormat.format("怪物{0}攻击玩家,玩家{1} HP减少{2}",
                monster.getName(),player.getName(),monsterHarmValue
                ));


        playerDataService.isPlayerDead(player,monster);

    }

}
