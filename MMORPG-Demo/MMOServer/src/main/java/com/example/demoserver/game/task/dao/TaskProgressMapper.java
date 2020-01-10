package com.example.demoserver.game.task.dao;

import com.example.demoserver.game.task.model.TaskProgress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskProgressMapper {


    TaskProgress selectByPrimaryId(TaskProgress taskProgress);

    List<TaskProgress> selectByPlayerId(Integer playerId);

    int insertTaskProgress(TaskProgress taskProgress);

    int updateTaskProgressByPrimaryKey(TaskProgress taskProgress);

    int deleteTaskProgressByPrimaryKey(TaskProgress taskProgress);

}
