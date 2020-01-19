package com.example.demoserver.game.gamecopy.model;

import com.example.demoserver.game.SceneEntity.model.Monster;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.scene.model.GameScene;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Data
public class GameCopy extends GameScene {


    private Integer gameCopyId;

    private Integer needPower;

    private Long lastTime;

    private String passReward;

    private String copyGameName;

    private Integer maxPlayerNum;

    private Integer boss;

    private String monsters;

    private Integer sceneId;

    private Integer type;


    private Map<Integer, Player> playerMap=new ConcurrentHashMap<>();

    private List<Monster> monsterList = new CopyOnWriteArrayList<>();

    private Monster bossMonster;


    // 是否已经挑战副本失败
    private volatile Boolean fail = false;


    private static ThreadFactory sceneThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("copy-loop-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    /** 通过一个场景一个线程处理器的方法保证每个场景的指令循序 */
    ScheduledExecutorService singleThreadSchedule = Executors.newSingleThreadScheduledExecutor(sceneThreadFactory);



}
