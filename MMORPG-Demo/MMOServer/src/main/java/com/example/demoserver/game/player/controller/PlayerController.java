package com.example.demoserver.game.player.controller;


import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.player.service.PlayerLoginService;
import com.example.demoserver.game.player.service.PlayerQuitService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.game.user.model.User;
import com.example.demoserver.game.user.service.UserService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
@Controller
@Slf4j
public class PlayerController {


    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private PlayerLoginService playerLoginService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameSceneService gameSceneService;

    @Autowired
    private PlayerQuitService playerQuitService;




        @RequestMapping(getOrder = Orders.CREATEPLAYER)
        public void roleCreate(ChannelHandlerContext ctx, Msg message) {
        User user = userService.getUserByCxt(ctx);
        if (Objects.isNull(user)) {
            Notify.notifyByCtx(ctx,"创建角色前需要登陆账号");
            return;
        }

        String[] args = SplitParameters.split(message);
        String roleName = args[1];
        Integer role = Integer.valueOf(args[2]);
        playerLoginService.roleCreate(ctx,roleName,role,user.getId());


    }

    /**
     *  游戏角色登陆
     * @param ctx 上下文
     * @param message 信息
     */

    @RequestMapping(getOrder = Orders.SELECTPLAYER)
    public void  playerLogin(ChannelHandlerContext ctx, Msg message) {
        String[] array = SplitParameters.split(message);
        Integer playerId = Integer.valueOf(array[1]);
        StringBuilder result = new StringBuilder();


        //判断用户是否有此职业角色
        if (userService.isUserOnline(ctx) && playerLoginService.hasPlayerRole(ctx, playerId) ){

            Player player = playerLoginService.login(playerId,ctx);

            GameScene gameScene = player.getCurrentScene();

            result.append(player.getName()).append(",角色登陆成功")
                    .append("\n 你所在位置为: ")
                    .append(gameScene.getName()).append("\n")
                    .append("相邻的场景是： ");
            List<GameScene> gameSceneList = gameSceneService.getNeighborsSceneByIds(gameScene.getNeighborScence());
            gameSceneList.forEach(neighbor -> {
                result.append(neighbor.getId()).append(": ").append(neighbor.getName()).append(", ");
            });


        } else {
            result.append("用户尚未登陆，不能加载角色");
        }
        Notify.notifyByCtx(ctx,result);
    }


    @RequestMapping(getOrder = Orders.EXIT)
    public void playerQuit(ChannelHandlerContext ctx, Msg message) {

        // 断开连接退出
        playerQuitService.logout(ctx);
    }



}
