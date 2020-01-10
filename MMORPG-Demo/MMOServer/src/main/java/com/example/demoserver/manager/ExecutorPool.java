package com.example.demoserver.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ExecutorPool {


    public static ExecutorService handleThreadPool =null;

    public static void initThreadsExecuytor(){
        handleThreadPool= Executors.newCachedThreadPool();
    }

    public static void execute(Runnable runnable){
        handleThreadPool.execute(runnable);
    }

    public static void shautdown(){
        if(!handleThreadPool.isShutdown()){
            handleThreadPool.shutdown();
        }
    }
}
