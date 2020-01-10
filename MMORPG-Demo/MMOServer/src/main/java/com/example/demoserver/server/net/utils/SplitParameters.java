package com.example.demoserver.server.net.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.game.bag.model.Item;
import com.example.demoserver.game.bag.model.ItemInfo;
import com.example.demoserver.game.bag.model.ItemProperty;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;


//分割工具
public class SplitParameters {

    public static String[] split(Msg message){


        String[] param =message.getContent().split("\\s+");

        return param;
    }

}
