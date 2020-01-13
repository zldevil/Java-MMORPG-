package com.example.demoserver.common.commons;

public interface Constant {


    /**
     * 新玩家默认场景id
     */
    public static Integer DEFAULT_SENCE_ID = 1;

    /**
     * 非角色单位码
     */
    public static Integer NPC = 1;

    /**
     * 怪物类型码
     */
    public static Integer Monster = 2;

    /**
     * 系统错误响应吗
     */
    public static Integer SVR_ERR = 500;

    /**
     * 指令错误响应码
     */
    public static Integer ORDER_ERR = 404;


    /**
     * 击杀怪物的经验
     */
    public static Integer MONSTER_EXP = 10;

    /**
     * 等级除数
     */
    public static Integer LEVEL_DIVISOR = 100;

    public static int SUCCESS = 200;

    int EQUIP_COMSUM_DURABLE = 5;

    Integer EXPITEM_BUFF = 1000;
    Integer EXP_ITEMINFO = 2000;

    Integer packageSize=16;
    Long MAXMP=600L;
    Long MAXHP=600L;

}
