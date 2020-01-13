package com.example.demoserver.game.equip.controller;

import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.service.BagService;
import com.example.demoserver.game.equip.model.EquitmentPart;
import com.example.demoserver.game.equip.service.EquipmentBarService;
import com.example.demoserver.game.player.model.Player;
import com.example.demoserver.game.player.service.PlayerDataService;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.notify.Notify;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

@Controller
@Slf4j
@Component
public class EquipmentController {


    @Autowired
    private EquipmentBarService equipmentBarService;

    @Autowired
    private PlayerDataService playerDataService;

    @Autowired
    private BagService bagsService;

    @Autowired
    private Notify notify;


    @RequestMapping(getOrder = Orders.WEAREQUIP)
    private void wearEquip(ChannelHandlerContext ctx, Msg message) {
        String[] command = SplitParameters.split(message);
        Long equipmentId = Long.valueOf(command[1]);
        Player player = playerDataService.getPlayer(ctx);
        ItemInfo itemInfo = player.getBag().getItemMap().get(equipmentId).getItemInfo();

        boolean flag = equipmentBarService.wearEquip(player, equipmentId);

        if (flag) {
            Notify.notifyByCtx(ctx, MessageFormat.format("装备 {0}  成功",
                    itemInfo.getName()));

        } else {
            Notify.notifyByCtx(ctx, "装备失败");
        }
    }


    @RequestMapping(getOrder = Orders.SHOW_EQUIPMENT_BAR)
    private void showEquip(ChannelHandlerContext ctx, Msg message) {
        Player player = playerDataService.getPlayerByCtx(ctx);

        equipmentBarService.showEquip(player);

    }

    @RequestMapping(getOrder = Orders.REMOVE_EQUIP)
    private void removeEquip(ChannelHandlerContext ctx, Msg message) {
        String[] params= SplitParameters.split(message);
        // 需要卸下装备的部位
        String part = params[1];

        Player player = playerDataService.getPlayerByCtx(ctx);
        equipmentBarService.removeEquip(player, part);


    }
}
