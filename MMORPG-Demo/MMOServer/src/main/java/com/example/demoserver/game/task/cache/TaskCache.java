package com.example.demoserver.game.task.cache;

import com.example.demoserver.game.task.model.Task;
import com.example.demoserver.server.net.utils.ExcelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskCache {
    public static Cache<Integer, Task> taskCache = CacheBuilder.newBuilder().removalListener(
            notification -> log.debug(notification.getKey() + "被移除,原因是" + notification.getCause())
    ).build();


    @PostConstruct
    public void init(){

        //出错
        try {
            List<Task> taskList  = ExcelUtil.readExcel("static/task.xlsx",new Task());

            taskList.forEach(task -> {
                taskCache.put(task.getId(),task);
            });
            log.info("任务加载完成");

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Task加载出错");
        }

    }

    public Task getTask(Integer taskId){
        return taskCache.getIfPresent(taskId);
    }

    public void setTask(Integer taskId,Task task){
        taskCache.put(taskId,task);
    }


}
