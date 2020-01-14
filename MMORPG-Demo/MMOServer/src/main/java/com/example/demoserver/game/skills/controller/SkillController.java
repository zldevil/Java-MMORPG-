package com.example.demoserver.game.skills.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.skills.service.SkillService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Component
public class SkillController {



    @Resource
    private Notify notify;

    @Autowired
    private SkillService skillService;


    @RequestMapping(getOrder = Orders.ATTACKMONSTER)
    public  void useSkillAttackMonster(ChannelHandlerContext ctx, Msg message) {

        String[] command = SplitParameters.split(message);
        Integer skillId = Integer.valueOf(command[1]);

        List<Integer> targetIdList = Arrays.stream(Arrays.copyOfRange(command,2,command.length))
                .map(Integer::valueOf).collect(Collectors.toList());

        skillService.useSkillAttackMonster(ctx,skillId,targetIdList);
    }


}
