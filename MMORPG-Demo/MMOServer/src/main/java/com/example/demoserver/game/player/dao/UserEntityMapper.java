package com.example.demoserver.game.player.dao;

import com.example.demoserver.game.player.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper

@Repository
public interface UserEntityMapper {

    int insertUserEntity(UserEntity userEntity);

    UserEntity selectUserEntityById(int id);

    //查找同一用户下的角色列表
    List<UserEntity> selectUserEntityByUserID(int userId);


    //通过name查找
    UserEntity selectPlayerByName(String name);

}
