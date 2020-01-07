package com.example.demoserver.server.net.utils;

import java.util.Random;

public class IDUtil {

    public static  Integer getid(){
        Random random=new Random();
        Integer id =random.nextInt(999);
        return id;
    }
}
