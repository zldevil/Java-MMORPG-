package com.example.demoserver.game.task.model;

import java.util.List;

public enum TaskState {


    NOT_START("任务未接", 1),

    //任务已被领取
    RUNNING("进行中", 2),

    COMPLETE("任务完成未领取奖励", 3),

    FINISH("任务结束领取奖励", 4);

    // 任务完成了之后不再触发
    /*NEVER("不再触发",5);*/

    String name;

    Integer code;

    TaskState(String name ,Integer code){
        this.code=code;
        this.name=name;
    }

    public Integer getCode(){
        return code;
    }

    public TaskState getTaskStateByCode(Integer code){
      TaskState[] stateArray= TaskState.values();
      for(TaskState taskState:stateArray){
          if(taskState.code.equals(code)){
              return taskState;
          }
      }
        return NOT_START;
    }
}
