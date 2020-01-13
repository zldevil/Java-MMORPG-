package com.example.demoserver.game.friend.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.game.friend.service.FriendService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.utils.SplitParameters;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Controller
@Component
public class FriendController {

    @Resource
    private FriendService friendService;

    @Resource
    private PlayerDataService playerDataService;



    private void friendAdd(ChannelHandlerContext ctx, Msg message) {

        String[] args = SplitParameters.split(message);
        Integer friendId = Integer.valueOf(args[1]);
        Player player = playerDataService.getPlayerByCtx(ctx);
        friendService.addFriend(player,friendId);

    }

    private void friendList(ChannelHandlerContext ctx, Msg message) {

        Player player = playerDataService.getPlayerByCtx(ctx);
        friendService.listFriend(player);

    }
}
