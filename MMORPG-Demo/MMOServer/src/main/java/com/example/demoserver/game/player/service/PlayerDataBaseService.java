package com.example.demoserver.game.player.service;

import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.model.UserEntity;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class PlayerDataBaseService {


    @Autowired
    private UserEntityMapper userEntityMapper;

    /**
     *  持久化的线程池，直接用多线程的线程池。
     *
     */

    private  ThreadFactory persistenceThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("persistence-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    public  ExecutorService threadPool = new ThreadPoolExecutor(4,8,
            1000, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100), persistenceThreadFactory );



    public  void updatePlayerDataBase(Player player){
        threadPool.execute(()->{
            UserEntity userEntity =new UserEntity();
            BeanUtils.copyProperties(player,userEntity);
            userEntityMapper.updateUserEntity(userEntity);
        });
    }




}
