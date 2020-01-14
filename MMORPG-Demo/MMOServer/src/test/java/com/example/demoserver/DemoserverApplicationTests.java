package com.example.demoserver;

import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.task.cache.TaskCache;
import com.example.demoserver.game.user.dao.UserMapper;
import com.example.demoserver.server.net.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@SpringBootTest
@Slf4j
class DemoserverApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    SpringUtil springUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserEntityMapper userEntityMapper;

    @Autowired
    TaskCache taskCache;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection=dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }




}
