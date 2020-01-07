package com.example.demoserver.server.net.utils;

public class BeanNameUtil {
    public static Object getBean(Class<?> controller) {

        String name = controller.getName();
        Object resultBean = null;
        switch (name) {
            case "com.example.demoserver.game.player.controller.PlayerController":
                resultBean = SpringUtil.getBean("playerController");
                break;
            case "com.example.demoserver.game.scence.controller.AOIController ":
                resultBean = SpringUtil.getBean("aOIController");
                break;
            case "com.example.demoserver.game.scence.controller.SceneController":
                resultBean = SpringUtil.getBean("sceneController");
                break;
            case "com.example.demoserver.game.ScenceEntity.controller.SceneObjectController":
                resultBean = SpringUtil.getBean("sceneObjectController");
                break;
            case "com.example.demoserver.game.user.controller.UserController":
                resultBean = SpringUtil.getBean("userController");
                break;

        }
        return resultBean;
    }
}

