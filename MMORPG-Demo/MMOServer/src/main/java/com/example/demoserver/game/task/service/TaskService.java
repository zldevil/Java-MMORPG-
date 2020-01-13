package com.example.demoserver.game.task.service;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.common.commons.Character;
import com.example.demoserver.event.dispatch.EventManager;
import com.example.demoserver.event.events.ActiveTaskEvent;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.task.cache.TaskCache;
import com.example.demoserver.game.task.dao.TaskProgressMapper;
import com.example.demoserver.game.task.model.*;
import com.example.demoserver.server.notify.Notify;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {


    @Autowired
    public TaskCache taskCache;

    @Autowired
    public Notify notify;

    @Autowired
    public TaskProgressMapper taskProgressMapper;

    @Autowired
    public BagService bagService;

    /**
     * @param player
     */
    public void showAllTask(Player player){
        List<Task> taskList=TaskCache.taskCache.asMap().values().stream().collect(Collectors.toList());
        StringBuilder stringBuilder=new StringBuilder();
        taskList.forEach(task -> {
            stringBuilder.append(MessageFormat.format(" 任务ID:{0} 任务名称为{1} 任务奖励为{2} 接受任务需要达到{3}级以上",
                    task.getId(),task.getName(),task.getRewardDescription(),task.getLevel()));
            stringBuilder.append("\n");
        });
        notify.notifyPlayer(player,stringBuilder);
    }


    /**
     * 使用showtask查看当前正在执行的任务以及进度
     */
    public List<TaskProgress> showAcceptingTaskAndProgress(Player player) {
        return player.getTaskProgressMap().values()
                .stream()
                .filter(t -> t.getTaskState().equals(TaskState.RUNNING))
                .collect(Collectors.toList());
    }


    /**
     *
     * @param player
     * @param taskId
     */
    public void acceptTask(Player player,Integer taskId){

        if(taskCache.getTask(taskId)==null){
            notify.notifyPlayer(player,"不存在此任务");
        }

        TaskProgress taskProgress =player.getTaskProgressMap().get(taskId);

        if(Objects.nonNull(taskProgress) && taskProgress.getTaskState().equals(TaskState.FINISH.getCode())){

            notify.notifyPlayer(player,"已经完成此任务并领取过奖励");
        }

        Task task=taskCache.getTask(taskId);
        // 新建创建任务进度
        TaskProgress questProgress = createTaskProgress(task,player);

        if(Objects.nonNull(questProgress)) {
            notify.notifyPlayer(player,MessageFormat.format("接受任务 {0}  ",
                    task.getName()));
        }

    }

    /**
     * 获取一个任务进度，如果玩家任务进度尚未记录，新建一个并持久化
     * @param task 任务
     * @param player 玩家
     * @return 一个任务进度
     */

    public TaskProgress createTaskProgress(Task task, Player player) {
        // 获取玩家的任务进度
        TaskProgress taskProgress = player.getTaskProgressMap().get(task.getId());

        // 如果玩家没有记录，现在创建记录
        if (Objects.isNull(player.getTaskProgressMap().get(task.getId()))) {
            TaskProgress taskProgressNow = new TaskProgress();
            taskProgressNow.setTaskId(task.getId());
            taskProgressNow.setPlayerId(player.getId());
            taskProgressNow.setBeginTime(System.currentTimeMillis());
            taskProgressNow.setTask(task);
            taskProgressNow.setTaskState(TaskState.RUNNING.getCode());

            // 初始化进度
            Progress progress =new Progress(task.getFinishConditionObject());

            taskProgressNow.setProgress(JSON.toJSONString(progress));

            // 将任务进度放入玩家当中并持久化进度
            player.getTaskProgressMap().put(taskProgressNow.getTaskId(),taskProgressNow);

            saveOrUpdateProgress(taskProgressNow);

            taskProgress = taskProgressNow;
        }

        log.debug("taskProgress {}",taskProgress);

        return taskProgress;
    }

    /**
     *
     * @param player
     * @param taskId
     */
    public void finishTask(Player player,Integer taskId){

        TaskProgress taskProgress=player.getTaskProgressMap().get(taskId);

        if(null==taskProgress){
            notify.notifyPlayer(player,"您还没有领取该任务");
        }

        if(taskProgress.getTaskState().equals(TaskState.FINISH.getCode())){
            notify.notifyPlayer(player,"您已经做完该任务，领取过奖励");
        }

        if(taskProgress.getTaskState().equals(TaskState.COMPLETE.getCode())){
            taskProgress.setEndTime(System.currentTimeMillis());
            taskProgress.setTaskState(TaskState.FINISH.getCode());

            //数据库操作
            saveOrUpdateProgress(taskProgress);

            TaskReward taskReward =taskProgress.getTask().getTaskReward();
            List<TaskReward.RewardItem> itemList = taskReward.getItemList();
            for (TaskReward.RewardItem rewardItem : itemList) {
                ItemInfo itemInfo = bagService.getItemInfo(rewardItem.getItemInfoId());
                Item item = bagService.createItemByItemInfo(itemInfo.getId(), rewardItem.getNum());
                bagService.addItem(player, item);
            }
            List<Integer> nextTask = taskReward.getNextTask();
            if(nextTask.size() != 0) {
                EventManager.publish(new ActiveTaskEvent(player, nextTask));
            }

            Integer exp=taskReward.getExp();
            player.setExp(player.getExp()+exp);

        }else {
            notify.notifyPlayer(player,"任务还没完成");
        }
    }

    public void giveUpTaask(Player player,Integer taskId){

        removeTaskProgress(player,taskId);
    }


    /**
     * load任务进度到玩家实体,初始化时使用
     */
    public void loadTaskProgress(Player player) {

        List<TaskProgress> taskProgresses = taskProgressMapper.selectByPlayerId(player.getId());

        for (TaskProgress taskProgress : taskProgresses) {
            Task task = taskCache.getTask(taskProgress.getTaskId());
            taskProgress.setTask(task);

            player.getTaskProgressMap().put(taskProgress.getTaskId(), taskProgress);
            if(taskProgress.getTaskState().equals(TaskState.RUNNING.getCode())) {
                notify.notifyPlayer(player,MessageFormat.format("当前正在进行的任务: {0} ",taskProgress.getTask().getName()));

            }
        }

        log.info("玩家任务进度加载完毕");
    }

    /**
     *  创建或更新一个玩家任务进度记录
     * @param progress 新创建的进度
     */
    public  void saveOrUpdateProgress(TaskProgress progress) {
        threadPool.execute(() -> {

           TaskProgress tQuestProgress = taskProgressMapper.selectByPrimaryId(progress);

            if (Objects.isNull(tQuestProgress)) {
                taskProgressMapper.insertTaskProgress(progress);
            } else {
                taskProgressMapper.updateTaskProgressByPrimaryKey(progress);
            }
        });
    }


    /**
     *  移除数据库玩家任务进程
     * @param player 玩家id
     * @param taskId 任务id
     */

    public void removeTaskProgress(Player player, Integer taskId) {

        threadPool.execute(() -> taskProgressMapper.deleteTaskProgressByPrimaryKey(player.getTaskProgressMap().get(taskId)) );
    }


    /**
     * 新玩家初始化任务
     * @param initedPlayer
     */
    public void getNewPlayerTask(Player initedPlayer) {

        addAcceptTask(initedPlayer, 1);

    }

    //增加任务
    public void addAcceptTask(Player player, Integer taskId) {
        if(player.getTaskProgressMap().get(taskId) != null) {
            return;
        }
        Task task = taskCache.getTask(taskId);
        TaskProgress taskProgress = createTaskProgress(task,player);

        //插入数据库
        if(taskProgressMapper.insertTaskProgress(taskProgress) <= 0) {
            log.info("progress插入失败");
        }

        player.getTaskProgressMap().put(taskId, taskProgress);

        notify.notifyPlayer(player,MessageFormat.format("解锁新任务 id为 {0},任务名称：{1} ",task.getId(),task.getName()));

    }

    //检测任务进度
    public void checkTaskProgress(Player player, Character character){
        player.getTaskProgressMap().values().forEach(taskProgress -> {
           if(taskProgress.getProgressObject().getCondition().getTarget().equals(character.getId())){
                taskProgress.getProgressObject().addProgressNum(1);
                if(taskProgress.getProgressObject().finished){
                    //通知任务完成
                }
            }
        });
    }

    /**
     *  持久化的线程池，由于持久化不需要保证循序，所以直接用多线程的线程池。
     *  线程数 为 服务器核心*2+1
     */

    private static ThreadFactory persistenceThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("persistence-%d").setUncaughtExceptionHandler((t,e) -> e.printStackTrace()).build();
    public static ExecutorService threadPool = new ThreadPoolExecutor(4,8,
            1000, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100), persistenceThreadFactory );

}
