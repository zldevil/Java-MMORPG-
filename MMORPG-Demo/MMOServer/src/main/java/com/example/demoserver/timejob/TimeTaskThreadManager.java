package com.example.demoserver.timejob;

import com.example.demoserver.event.common.GameEvent;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class TimeTaskThreadManager {


    /**
     * 执行游戏过程中用到的任务
     */
    private static ThreadFactory scheduledThreadPoolFactory = new ThreadFactoryBuilder()
            .setNameFormat("scheduledThreadPool-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    private static ScheduledExecutorService threadPool =
            Executors.newScheduledThreadPool( Runtime.getRuntime().availableProcessors()*2+1,scheduledThreadPoolFactory);



    private static ThreadFactory singleThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("singleThread-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    public static ScheduledExecutorService singleThreadSchedule =  Executors.newSingleThreadScheduledExecutor(singleThreadFactory);



    /**
     *  设置定时任务
     * @param delay 延迟执行时间，单位毫秒
     * @param callback 任务
     * @return 返回结果的多线程定时任务
     */

    public static Future<GameEvent> threadPoolSchedule(long delay, Callable<GameEvent> callback) {
        return threadPool.schedule(callback,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  设置单线程定时任务
     * @param delay 延迟执行时间，单位毫秒
     * @param runnable 任务
     * @return
     */
    public static Future singleThreadSchedule(long delay, Runnable runnable) {
        return singleThreadSchedule.schedule(runnable,delay, TimeUnit.MILLISECONDS);
    }



    /**
     *  设置多线程定时任务
     * @param delay 延迟执行0时间，单位毫秒
     * @param runnable 任务
     * @return 不返回结果的多线程定时任务
     */
    public static Future threadPoolSchedule(long delay, Runnable runnable) throws Exception{
        return threadPool.schedule(runnable,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  按固定的周期执行任务
     * @param initDelay 延时开始第一次任务的时间
     * @param delay     执行间隔
     * @param runnable 任务
     * @return
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(long initDelay , long delay , Runnable runnable) throws Exception{
        return threadPool.scheduleAtFixedRate(runnable,initDelay,delay, TimeUnit.MILLISECONDS);
    }


    /**
     *  按照固定的延迟执行任务（即执行完上一个再执行下一个）
     * @param initDelay 延时开始第一次任务的时间
     * @param delay     执行间隔
     * @param runnable 任务
     *
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(long initDelay , long delay , Runnable runnable) {
        return threadPool.scheduleWithFixedDelay(runnable,initDelay,delay, TimeUnit.MILLISECONDS);
    }
}
