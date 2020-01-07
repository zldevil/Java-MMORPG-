package com.example.demoserver;

import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.user.dao.UserMapper;
import com.example.demoserver.game.user.model.User;
import com.example.demoserver.server.net.utils.SpringUtil;
import com.mysql.cj.jdbc.SuspendableXAConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@SpringBootTest
class DemoserverApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    SpringUtil springUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserEntityMapper userEntityMapper;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection=dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }
    @Test
    void test(){
        ApplicationContext applicationContext=SpringUtil.getApplicationContext();
        System.out.println(applicationContext.getClass());
    }


    @Test
    public void tesf(){

        UserEntity userEntity =new UserEntity();
        userEntity.setId(1);
        userEntity.setName("残雪");
        userEntityMapper.insertUserEntity(userEntity);
        System.out.println(userEntityMapper.insertUserEntity(userEntity));

    }

    @Test
    public void testSql(){

        System.out.println(userEntityMapper.selectPlayerByName("残雪"));


    }

}
