package com.example.demoserver.game.task.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demoserver.game.player.model.Player;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TaskProgress {


    private  Integer playerId;

    private  Integer taskId;

    private Integer taskState;

    private Long beginTime;

    private Long endTime;

    private String progress;

    //并非存储的属性
    Task task;


    public TaskProgress(Player player, Task task) {
        this.playerId = player.getId();
        this.beginTime = System.currentTimeMillis();
        taskState = TaskState.NOT_START.getCode();
        taskId = task.getId();
        this.task = task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    Progress progressObject=new Progress();

    public Progress getProgressObject() {

        progressObject=JSON.parseObject(progress,new TypeReference<Progress>(){});
        return progressObject;
    }


}
