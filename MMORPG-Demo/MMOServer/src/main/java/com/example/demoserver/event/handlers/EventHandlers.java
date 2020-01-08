package com.example.demoserver.event.handlers;

import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.event.events.LevelEvent;
import com.example.demoserver.event.events.MonsterDeadEvent;
import com.example.demoserver.event.events.TalkEvent;
import com.example.demoserver.event.events.WearEquipmentEvent;
import com.example.demoserver.game.ScenceEntity.model.Monster;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class EventHandlers {

    {
        EventManager.registe(LevelEvent.class, this::levelUp);

        log.info("角色升级事件注册成功");

        EventManager.registe(MonsterDeadEvent.class, this::monsterDead);
        log.info("怪物死亡事件注册成功");

        EventManager.registe(TalkEvent.class, this::talk);
        log.info("聊天事件注册成功");

        EventManager.registe(WearEquipmentEvent.class, this::equipCheck);
        log.info("穿戴装备事件注册成功");

    }

    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    /**
     * 玩家跟npc对话
     */
    private void talk(TalkEvent conversationEvent) {
       /* taskService.checkTaskProgress(conversationEvent.getPlayer(),
                TaskType.CONVERSATION,
                FinishField.ENTITY_TYPE,
                conversationEvent.getNpc().getEntityTypeId(),
                progress -> progress.addProgressNum(1));*/
    }

    /**
     * 玩家升级事件处理
     */
    private  void levelUp(LevelEvent levelEvent) {
        Optional.ofNullable(levelEvent.getPlayer()).ifPresent(player -> {
            Integer level = player.getLevel();
            Integer newlevel = levelEvent.getLevel();
            player.setLevel(newlevel);

            ChannelHandlerContext channelHandlerContext=playerCacheMgr.getCxtByPlayerId(player.getId());
            Notify.notifyByCtx(channelHandlerContext, MessageFormat.format("恭喜你升级{0}级，当前等级为{1}",(newlevel-level),newlevel));

          /*  taskService.checkTaskProgress(player,
                    TaskType.FUNCTIONAL_UPGRADE,
                    FinishField.PLAYER_LEVEL,
                    LevelTarget.PLAYER.getCode(),
                    progress -> progress.setProgressNumber(newlevel));*/
        });
    }

    /**
     * 装备更换事件
     */
    private void equipCheck(WearEquipmentEvent wearEquipEvent) {
        Player player = wearEquipEvent.getPlayer();
        Long count = player.getEquipmentBar().values().stream().map(equip -> equip.getLevel()).count();

      /*  taskService.checkTaskProgress(player,
                TaskType.FUNCTIONAL_UPGRADE,
                FinishField.ALL_EQUIP_LEVEL,
                LevelTarget.EQUIPALL.getCode(),
                progress -> progress.setProgressNumber(count.intValue()));*/
    }



    /**
     * 怪物死亡事件处理 注意，不在这设置monster的属性
     */
    private  void monsterDead(MonsterDeadEvent monsterDeadEvent) {

        //玩家 检查任务进度，掉落物品，触发幸运大爆 ....
        //掉落物品
        Player attacter = monsterDeadEvent.getAttacter();
        Monster deadMonster = monsterDeadEvent.getTargetMonster();

       /* List<Item> dropItems = dropPool
                .getDropItems(attacter, deadMonster);

        dropItems.forEach(item -> {
            senceService.notifyPlayerByDefault(attacter, "掉落" + item.getItemInfo().getName()
                    + " * " + item.getCount());
            bagService.addItemInBag((Player) attacter, item);
        });
*/
        //检查任务进度
        /*taskService.checkTaskProgress(attacter,
                TaskType.KILLING,
                FinishField.ENTITY_TYPE,
                deadMonster.getEntityTypeId().intValue(),
                progress -> progress.addProgressNum(1));*/
    }

}
