package com.example.demoserver.game.task.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Task {


    private Integer id;
    private String name;
    //JSON串
    private String finishCondition;
    //JSON串
    private String reward;
    private String taskDescription;
    private Integer level;
    private Integer type;


    //任务完成条件改成一个JSON串
    private FinishCondition finishConditionObject = new FinishCondition();


    public FinishCondition getFinishConditionObject() {

        finishConditionObject = JSON.parseObject(finishCondition, new TypeReference<FinishCondition>() {
        });
        return finishConditionObject;
    }

    private TaskReward taskReward;

    public TaskReward getTaskReward() {
        if (taskReward == null && !Strings.isNullOrEmpty(reward)) {
            taskReward = (TaskReward) JSON.toJSON(reward);
        }
        return taskReward;
    }
}










