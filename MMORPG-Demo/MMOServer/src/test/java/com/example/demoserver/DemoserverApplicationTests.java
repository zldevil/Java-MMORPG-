package com.example.demoserver;

import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.roleproperty.model.RoleProperty;
import com.example.demoserver.game.shop.model.Shop;
import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.game.task.cache.TaskCache;
import com.example.demoserver.game.task.model.Task;
import com.example.demoserver.game.user.dao.UserMapper;
import com.example.demoserver.game.user.model.User;
import com.example.demoserver.server.net.utils.ExcelUtil;
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

    @Autowired
    TaskCache taskCache;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection=dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }



    @Test
    public void testRoleProperty(){
        try {
            List<RoleProperty> rolePropertyList = ExcelUtil.readExcel("static/roleProperties.xlsx",new RoleProperty());
            System.out.println(rolePropertyList );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSkill(){

        try {
            List<Shop> skillList = ExcelUtil.readExcel("static/shop.xlsx",new Shop());
            System.out.println(skillList);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生错误");
        }

    }







}
