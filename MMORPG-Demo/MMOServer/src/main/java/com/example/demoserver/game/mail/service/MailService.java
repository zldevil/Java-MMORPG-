package com.example.demoserver.game.mail.service;

import com.alibaba.fastjson.JSON;
import com.example.demoserver.game.bag.model.Bag;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.mail.dao.MailMapper;
import com.example.demoserver.game.mail.model.Mail;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.model.UserEntity;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.utils.IDUtil;
import com.example.demoserver.server.notify.Notify;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class MailService {

    @Autowired
    private BagService bagService;
    @Autowired
    private Notify notify;

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private PlayerDataService playerDataService;


    public void sendMail(Player player,Player targerPlayer, String  mailContent,Long itemId,Integer itemNum,Integer money){

        Bag bag=player.getBag();
        Item item=bag.getItemMap().get(itemId);
        if(item.getCount()<itemNum){
            notify.notifyPlayer(player, MessageFormat.format("物品{0}数量不足，拥有此物品数量为",
                    item.getItemInfo().getName(),item.getCount()));
        }

        bagService.removeItem(player,itemId,itemNum);

        TODO 保存玩家背包信息;

        Mail mail=new Mail();
        mail.setId(IDUtil.getid());
        mail.setSenderId(player.getId());
        mail.setReceiverId(targerPlayer.getId());
        mail.setContent(mailContent);
        mail.setMoney(money);
        mail.setAttachment(JSON.toJSONString(item));

        mailMapper.insertMail(mail);

        notify.notifyPlayer(player,"发送邮件成功");
    }


    public void listMails(Player player){

        List<Mail> mailList=mailMapper.selectMailsByReceivePlayerId(player.getId());
        notify.notifyPlayer(player,"----------邮件列表-----------");
        mailList.forEach(mail -> {
            UserEntity userEntity=playerDataService.getPlayerById(mail.getSenderId());
            notify.notifyPlayer(player,MessageFormat.format("邮件：{0}  发件人：{1} 内容为:{2} 附件:{3} 金币:{4}\n",
                    mail.getId(),userEntity.getName(),mail.getContent(),mail.getMoney()));
        });
    }


    public void receiveMail(Player player,Integer targetMailId){
        Mail mail = mailMapper.selectByPrimaryKey(targetMailId);
        if (mail.getReceiverId().equals(player.getId())){
            String itemString = mail.getAttachment();
            Item item = JSON.parseObject(itemString,Item.class);
            if (item == null) {
                notify.notifyPlayer (player,"邮件并没有附件或者附件已经取走");
            }
            if (bagService.addItem(player,item)) {
                // 更新邮件状态
                mail.setAttachment(null);
                mail.setHasRead(true);
                mailMapper.updateByPrimaryKey(mail);
                notify.notifyPlayer (player,"接收邮件成功，附件已经在你的背包了");

            } else {
                notify.notifyPlayer (player,"接收收件被拒绝，可能是背包空间不足");

            }
        } else {
            notify.notifyPlayer (player,"你的邮箱里并没有这封邮件");

        }
    }

}
