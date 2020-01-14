package Test;

import com.example.demoserver.game.scene.controller.AoiController;
import com.example.demoserver.server.net.utils.BeanNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class testRandom {


    @Test
    public void testDispatcher(){

        //Object handler= SpringUtil.getBean("sceneController");
        Object handler= BeanNameUtil.getBean(AoiController.class);
        if(handler==null){
            log.debug("handler为空");
        }
        System.out.println(handler);
    }
}
