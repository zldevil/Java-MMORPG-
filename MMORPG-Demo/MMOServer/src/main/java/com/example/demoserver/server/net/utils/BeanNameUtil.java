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
            case "com.example.demoserver.game.bag.controller.BagController":
                resultBean=SpringUtil.getBean("bagController");
                break;
            case "com.example.demoserver.game.bag.controller.ItemController":
                resultBean=SpringUtil.getBean("itemController");
                break;
            case "com.example.demoserver.game.chat.controller.ChatController":
                resultBean=SpringUtil.getBean("chatController");
                break;
            case "com.example.demoserver.game.chat.controller.EquipmentController":
                resultBean=SpringUtil.getBean("equipmentController");
                break;
            case "com.example.demoserver.game.friend.controller.FriendController":
                resultBean=SpringUtil.getBean("friendController");
                break;
            case "com.example.demoserver.game.guild.controller.GuildController":
                resultBean=SpringUtil.getBean("guildController");
                break;
            case "com.example.demoserver.game.shop.controller.ShopController":
                resultBean=SpringUtil.getBean("shopController");
                break;
            case "com.example.demoserver.game.skills.controller.SkillController":
                resultBean=SpringUtil.getBean("skillController");
                break;
            case "com.example.demoserver.game.task.controller.TaskController":
                resultBean=SpringUtil.getBean("taskController");
                break;
        }
        return resultBean;
    }
}

