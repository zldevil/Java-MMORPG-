package com.example.demoserver.game.user.dao;


import com.example.demoserver.game.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {


    User selectByPrimaryKey(int id);

    int insertUser(User user);

}
