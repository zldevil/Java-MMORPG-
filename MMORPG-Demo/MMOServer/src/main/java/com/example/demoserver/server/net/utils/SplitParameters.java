package com.example.demoserver.server.net.utils;

import com.example.demoserver.common.MsgEntity.Msg;


//分割工具
public class SplitParameters {

    public static String[] split(Msg message){


        String[] param =message.getContent().split("\\s+");

        return param;
    }
}
