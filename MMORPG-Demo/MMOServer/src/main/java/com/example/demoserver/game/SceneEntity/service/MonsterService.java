package com.example.demoserver.game.SceneEntity.service;

import com.example.demoserver.common.commons.Character;
import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.event.events.MonsterDeadEvent;
import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.skills.service.SkillService;
import com.example.demoserver.server.notify.Notify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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
            if (gameObjectService.sceneObjectAfterDead(monster)) {
                // 结算掉落，这里暂时直接放到背包里
                monsterDropsService.dropItem(player, monster);
                // 怪物死亡的事件
                EventManager.publish(new MonsterDeadEvent(player, monster));
            }
        }
    }
}
