package com.example.demoserver.game.buff.service;

import com.example.demoserver.common.commons.Character;
import com.example.demoserver.game.buff.dao.BuffCache;
import com.example.demoserver.game.buff.model.Buff;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.notify.Notify;
import com.example.demoserver.timejob.TimeTaskThreadManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class BuffService {


    @Autowired
    private BuffCache buffCache;

    @Autowired
    private Notify notify;

    @Autowired
    private PlayerDataService playerDataService;


    public void startBuffer(Character character, Buff buff) throws Exception {

        Buff characterBuffer = new Buff();

        BeanUtils.copyProperties(buff,characterBuffer);

        // 记录开始时间
        characterBuffer.setUseBuffTime(System.currentTimeMillis());

        character.getBufferList().add(characterBuffer);

        // 如果是buffer有不良效果
        if (buff.getEffect() != 0) {
            character.setState(buff.getEffect());
        }


        // 如果buffer有持续时间
        if (buff.getDuration() != 0) {
            // 如果buffer间隔触发
            if (buff.getIntervalTime() != 0) {
                Future cycleTask = TimeTaskThreadManager.scheduleAtFixedRate(0,buff.getIntervalTime(),

                        () -> {
                            character.setHp(character.getHp() + buff.getHpRecover());
                            character.setMp(character.getMp() + buff.getMpRecover());

                            // 如果是玩家，进行通知
                            if (character instanceof Player) {
                                notify.notifyPlayer((Player) character, MessageFormat.format(
                                        "你身上的buffer {0}  对你造成影响, hp:{1} ,mp:{2} \n",
                                        buff.getName(),buff.getHpRecover(),buff.getMpRecover()
                                ));

                                // 可能是负面效果，检测玩家是否死亡
                                playerDataService.isPlayerDead((Player) character,null);
                            }

                        }
                );

                TimeTaskThreadManager.threadPoolSchedule(buff.getDuration(),() -> {
                    cycleTask.cancel(true);
                    return null;
                });

            }


        } else{

            Future cycleTask = TimeTaskThreadManager.threadPoolSchedule(0,

                    () -> {
                        character.setHp(character.getHp() + buff.getHpRecover());
                        character.setMp(character.getMp() + buff.getMpRecover());

                        // 如果是玩家，进行通知
                        if (character instanceof Player) {
                            notify.notifyPlayer((Player) character, MessageFormat.format(
                                    "你身上的buffer {0}  对你造成影响, hp:{1} ,mp:{2} \n",
                                    buff.getName(),buff.getHpRecover(),buff.getMpRecover()
                            ));
                            // 可能是负面效果，检测玩家是否死亡
                            playerDataService.isPlayerDead((Player) character,null);
                        }

                    }
            );

            TimeTaskThreadManager.threadPoolSchedule(buff.getDuration(),() -> {
                cycleTask.cancel(true);
                return null;
            });
        }

            /**
             * buffer cd 处理
             */
            TimeTaskThreadManager.threadPoolSchedule(buff.getDuration(),
                    () -> {

                        // 过期移除buffer
                        character.getBufferList().remove(buff);

                        // 恢复正常状态
                        character.setState(1);

                        // 如果是玩家，进行通知
                        if (character instanceof Player) {
                            notify.notifyPlayer((Player) character,MessageFormat.format(
                                    "你身上的buffer {0}  结束\n",buff.getName()
                            ));
                            // 检测玩家是否死亡
                            //playerDataService.isPlayerDead((Player) character,null);
                        }
                        log.debug(" buffer过期清除定时器 {}", new Date());
                        return null;
                    });

    }


    public Buff getBuff(int BuffId) {
        return buffCache.get(BuffId);
    }


    public boolean removeBuffer(Player player, Buff Buff) {
        if (player != null && Buff != null) {
            player.getBufferList().remove(Buff);
            return true;
        } else {
            return false;
        }
    }
}
