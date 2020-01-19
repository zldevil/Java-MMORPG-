package com.example.demoserver.game.mail.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.mail.service.MailService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Controller
@Component
public class MailController {

    @Resource
    private MailService mailService;

    @Resource
    private PlayerDataService playerDataService;

    @Autowired
    private Notify notify;



    @RequestMapping(getOrder = Orders.SENDMAIL)
    public void sendMail(ChannelHandlerContext channelHandlerContext, Msg message){

        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);


        String[] args= SplitParameters.split(message);
        Integer targetId=Integer.valueOf(args[1]);
        String mailContent=args[2];
        Long itemId=Long .valueOf(args[3]);
        Integer itemNum=Integer.valueOf(args[4]);
        Integer money=Integer.valueOf(args[5]);

        if(money>player.getMoney()){
            notify.notifyPlayer(player,"发送的金币大于您目前拥有的金币，请调整金币数量");
            return;
        }

        Player targerPlayer=playerDataService.getPlayerById(targetId);
        if(null==targerPlayer){
            notify.notifyPlayer(player,"此玩家不存在，请重新输入玩家id");
        }

        mailService.sendMail(player,targerPlayer,mailContent,itemId,itemNum,money);

    }

    @RequestMapping(getOrder = Orders.RECEIVEMAIL)
    public void receiveMail(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        String[] args=SplitParameters.split(message);
        Integer targetMailId=Integer.valueOf(args[1]);
        mailService.receiveMail(player,targetMailId);
    }



    @RequestMapping(getOrder = Orders.MAILSLIST)
    public void listMails(ChannelHandlerContext channelHandlerContext,Msg message){
        Player player=playerDataService.getPlayerByCtx(channelHandlerContext);
        mailService.listMails(player);
    }

}
