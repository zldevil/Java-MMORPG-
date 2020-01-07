package com.example.demoserver.game.skills.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.game.scence.model.GameScene;
import com.example.demoserver.game.scence.servcie.GameSceneService;
import com.example.demoserver.game.skills.service.SkillService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
public class SkillController {


    @Resource
    private PlayerDataService playerDataService;

    @Resource
    private GameSceneService gameSceneService;

    @Resource
    private Notify notify;

    @Autowired
    private SkillService skillService;


    @RequestMapping(getOrder = Orders.ATTACKMONSTER)
    private void useSkillAttackMonster(ChannelHandlerContext ctx, Msg message) {

        String[] command = SplitParameters.split(message);
        Integer skillId = Integer.valueOf(command[1]);

        //Long targetId = Long.valueOf(command[2]);

        List<Integer> targetIdList = Arrays.stream(Arrays.copyOfRange(command,2,command.length))
                .map(Integer::valueOf).collect(Collectors.toList());

        skillService.useSkillAttackMonster(ctx,skillId,targetIdList);
    }


}
