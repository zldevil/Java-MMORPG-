package Test;

import com.example.demoserver.game.player.dao.UserEntityMapper;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.scene.controller.AoiController;
import com.example.demoserver.game.skills.model.Skill;
import com.example.demoserver.server.net.utils.BeanNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class testRandom {


    @Autowired
    UserEntityMapper userEntityMapper;

    @Test
    public void testDispatcher() {

        UserEntity userEntity=userEntityMapper.selectUserEntityById(109);
        userEntity.setSenceId(4);

        int a= userEntityMapper.updateUserEntity(userEntity);
        System.out.println(a);

    }
}
