package com.example.demoserver.game.task.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.manager.PlayerCacheMgr;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.task.cache.TaskCache;
import com.example.demoserver.game.task.model.TaskProgress;
import com.example.demoserver.game.task.service.TaskService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;


@Controller
@Component
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private PlayerCacheMgr playerCacheMgr;

    @Autowired
    private Notify notify;

    @Autowired
    TaskCache taskCache;



    @RequestMapping(getOrder = Orders.SHOWTASK)
    public void showAllTask(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerCacheMgr.getPlayerByCtx(channelHandlerContext);

        taskService.showAllTask(player);

    }


    /**
     * 查看正在进行中的任务
     * @param channelHandlerContext
     * @param message
     */
    @RequestMapping(getOrder = Orders.SHOWACCEPTINGTASKANDPROGRESS)
    public void showAcceptingTaskAndProgress(ChannelHandlerContext channelHandlerContext,Msg message){

        Player player=playerCacheMgr.getPlayerByCtx(channelHandlerContext);
        List<TaskProgress> taskProgressList=taskService.showAcceptingTaskAndProgress(player);

        StringBuilder stringBuilder =new StringBuilder();
        stringBuilder.append("正在进行的任务为");

        taskProgressList.forEach(taskProgress -> {
            stringBuilder.append(MessageFormat.format("任务名称为{0},  已完成{1}/{2} "
                    ,taskCache.getTask(taskProgress.getTaskId()).getName()
                    , taskProgress.getProgressObject().getProgressNum()
                    ,taskProgress.getProgressObject().getCondition().getGoal()));
            stringBuilder.append("\n");
        });

        notify.notifyPlayer(player,stringBuilder);

    }


    @RequestMapping(getOrder = Orders.ACCEPTTASK)
    public void acceptTask(ChannelHandlerContext ctx,Msg message){

        String[] args= SplitParameters.split(message);
        Player player=playerCacheMgr.getPlayerByCtx(ctx);

        taskService.acceptTask(player,Integer.valueOf(args[1]));

    }


    //结束任务，提交任务
    @RequestMapping(getOrder = Orders.FINISHTASK)
    public void finishTask(ChannelHandlerContext ctx,Msg message){

        Player player=playerCacheMgr.getPlayerByCtx(ctx);
        String[] args=SplitParameters.split(message);
        Integer taskId=Integer.valueOf(args[1]);
        taskService.finishTask(player,taskId);

    }

    public void giveUpTask(ChannelHandlerContext ctx,Msg message){

        Player player=playerCacheMgr.getPlayerByCtx(ctx);
        String[] args =SplitParameters.split(message);
        Integer taskId=Integer.valueOf(args[1]);
        taskService.giveUpTaask(player,taskId);
    }


}
