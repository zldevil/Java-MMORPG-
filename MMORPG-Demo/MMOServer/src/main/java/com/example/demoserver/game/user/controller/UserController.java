package com.example.demoserver.game.user.controller;


import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.manager.RoleCache;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.user.manager.UserCacheManger;
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

import java.text.MessageFormat;
import java.util.List;


@Slf4j
@Controller
@Component
public class UserController  {


    @Autowired
    private UserService userService;

    @Autowired
    private Notify notify;


    @RequestMapping(getOrder =Orders.CREATEUSER)
    public void userCreate(ChannelHandlerContext ctx, Msg message) {

        String[] args = SplitParameters.split(message);
        Integer userId = Integer.valueOf(args[1]);
        String password = args[2];
        userService.register(ctx,userId,password);

    }



    @RequestMapping(getOrder = Orders.LOGIN)
    public void userLogin(ChannelHandlerContext ctx, Msg message) {
        String[] array = SplitParameters.split(message);
        int userId =  Integer.valueOf(array[1]);
        String password = array[2];
        userService.IsCorrectIdAndPassword(userId,password,ctx);

    }



    @RequestMapping(getOrder = Orders.SHOWPLAYER)
    public void roleList(ChannelHandlerContext ctx, Msg message) {
        User user = UserCacheManger.getUserByCtx(ctx);
        List<UserEntity> list = userService.findPlayers(user.getId());
        StringBuilder sb = new StringBuilder();

        list.forEach(userEntity -> {
            sb.append(MessageFormat.format("id:{0}  名字:{1}  职业:{2}",
                    userEntity.getId(),userEntity.getName(), RoleCache.getRole(userEntity.getTypeId()).getRoleName()
            ));
        });

        Notify.notifyByCtx(ctx,sb.toString());
        Notify.notifyByCtx(ctx,"请使用`showplayer {id}` 的加载角色 ");

    }

}
