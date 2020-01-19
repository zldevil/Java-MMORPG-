package com.example.demoserver.game.gamecopy.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.game.SceneEntity.manager.GameObjectCache;
import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.SceneEntity.model.ScenceEntity;
import com.example.demoserver.game.SceneEntity.service.MonsterService;
import com.example.demoserver.game.gamecopy.cache.GameCopyCache;
import com.example.demoserver.game.gamecopy.model.CommonMonsterInCopy;
import com.example.demoserver.game.gamecopy.model.GameCopy;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.scene.manager.SceneCache;
import com.example.demoserver.server.notify.Notify;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class GameCopyService {

    @Autowired
    private Notify notify;

    @Autowired
    private GameObjectCache gameObjectCache;

    @Autowired
    private GameCopyCache gameCopyCache;

    @Autowired
    private MonsterService monsterService;

    public void showGameCopy(Player player){

        Map<Integer, GameCopy> gameCopyMap=GameCopyCache.gameCopyCache.asMap();

        gameCopyMap.values().forEach(gameCopy -> {
            notify.notifyPlayer(player,MessageFormat.format("场景{0} 有关卡副本{1},副本的boss为{2}",
                    SceneCache.getScene(gameCopy.getSceneId()),
                    gameCopy.getName(),
                    gameObjectCache.get(gameCopy.getBoss()).getName()));
        });
    }

    //个人的
    public void enterGameCopy(Player player,Integer gameCopyId){

        GameCopy gameCopy=createGameCopy(gameCopyId);

        gameCopy.getPlayerMap().put(player.getId(),player);

        player.setGameCopyId(gameCopyId);
        notify.notifyPlayer(player,MessageFormat.format("玩家{0}进入{1}副本",player.getName(),gameCopy.getName()));

        ScheduledFuture<?> attackTask=gameCopy.getSingleThreadSchedule().scheduleAtFixedRate(()->{

            gameCopy.getMonsterList().forEach(monster -> {
                monsterService.startAiAttackInCopy(monster,gameCopy);
            });

            monsterService.startAiAttackInCopy(gameCopy.getBossMonster(),gameCopy);

        },0,2000,TimeUnit.MILLISECONDS);

        isPassGameCopy(gameCopy);

    }

    //组队的
    public void enterTeamGameCopy(Collection<Player> playerList, Integer gameCopyId){

        GameCopy gameCopy=createGameCopy(gameCopyId);

        playerList.forEach(player -> {
            gameCopy.getPlayerMap().put(player.getId(),player);
            player.setGameCopyId(gameCopyId);
            notify.notifyPlayer(player,MessageFormat.format("玩家{0}进入{1}副本",player.getName(),gameCopy.getName()));
        });


        ScheduledFuture<?> attackTask=gameCopy.getSingleThreadSchedule().scheduleAtFixedRate(()->{

            gameCopy.getMonsterList().forEach(monster -> {
                monsterService.startAiAttackInCopy(monster,gameCopy);
            });

            monsterService.startAiAttackInCopy(gameCopy.getBossMonster(),gameCopy);

        },0,2000,TimeUnit.MILLISECONDS);

        isPassGameCopy(gameCopy);

    }


    /**
     *
     * @param player
     * @param gameCopy
     */
    public void exitGameCopy(Player player,GameCopy gameCopy){
        gameCopy.getPlayerMap().remove(player.getId());
        destoryGameCopy(player,gameCopy);
        notify.notifyPlayer(player,"你已退出副本");
        notify.notifyPlayer(player,MessageFormat.format("您当前在{0}",player.getCurrentScene().getName()));
    }


    public GameCopy createGameCopy(Integer gameCopyId){
        GameCopy gameCopy=new GameCopy();
        BeanUtils.copyProperties(gameCopyCache.get(gameCopyId),gameCopy);

        //加载怪物和boss
        loadBossMonsterToGameCopy(gameCopy,gameCopy.getBoss());
        loadCommonMonsterToGameCopy(gameCopy,gameCopy.getMonsters());

        return gameCopy;
    }


    public void loadBossMonsterToGameCopy(GameCopy gameCopy,Integer bossId){

        ScenceEntity scenceEntity=gameObjectCache.get(bossId);
        Monster bossMonster=new Monster();
        BeanUtils.copyProperties(scenceEntity,bossMonster);
        gameCopy.setBossMonster(bossMonster);
    }


    public void loadCommonMonsterToGameCopy(GameCopy gameCopy,String monsters){

       List<CommonMonsterInCopy> commonMonsterInCopyList = JSON.parseObject(monsters,
               new TypeReference<List<CommonMonsterInCopy>>(){});

       List<Monster>monsterList=gameCopy.getMonsterList();
       commonMonsterInCopyList.forEach(commonMonsterInCopy -> {

           for(int count=0;count<commonMonsterInCopy.getNum();count++){
              ScenceEntity scenceEntity= gameObjectCache.get(commonMonsterInCopy.getSenceEntityId());
              Monster monster=new Monster();
              BeanUtils.copyProperties(scenceEntity,monster);
               monsterList.add(monster);
           }

       });

    }

    public void isPassGameCopy(GameCopy gameCopy){

        singleThreadSchedule.schedule(()->{

            Monster bossMonster=gameCopy.getBossMonster();
            List<Monster> monstersList=gameCopy.getMonsterList();
            if(bossMonster==null&&monstersList.size()==0){

                gameCopy.getPlayerMap().values().forEach(playerTmp ->{
                    notify.notifyPlayer(playerTmp,MessageFormat.format("恭喜你通过{0}副本",gameCopy.getName()));
                } );

            }else {
                gameCopy.getPlayerMap().values().forEach(playerTmp ->{
                    notify.notifyPlayer(playerTmp,MessageFormat.format("很遗憾，你没通过{0}副本",gameCopy.getName()));
                } );
            }


        },gameCopy.getLastTime(), TimeUnit.MILLISECONDS);

    }

    public void destoryGameCopy(Player player,GameCopy gameCopy){

        player.setGameCopyId(0);

        log.info("玩家退出，销毁副本");
        gameCopy=null;
    }



    ThreadFactory threadFactory= new ThreadFactoryBuilder().setNameFormat("gamecopy-loop-%d").build();

    ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(threadFactory);

}
