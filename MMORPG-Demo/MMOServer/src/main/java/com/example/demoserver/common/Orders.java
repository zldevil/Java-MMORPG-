package com.example.demoserver.common;

public enum  Orders {



    /**显示当前位置和可
     *
    以移动的地点*/
    LOCATION("location",150,"显示当前位置和相邻场景"),


    MOVE("move",151,"角色移动， 参数 场景id ， 例： move 2"),


    ErrOrder("errOrder",101 ,"出现错误，可能不存在此输入指令"),


    AOI("aoi",152,"AOI, 显示场景内各种游戏对象"),


    EXIT("exit",154,"玩家退出游戏"),


    CREATEPLAYER("createPlayer",155,"创建角色"),


    /**
     * "角色登录"*/
    SELECTPLAYER("selectplayer",157,"选择角色登录"),


    CREATEUSER("createUser",158,"注册用户"),


    SHOWPLAYER("showplayer",156,"显示用户所拥有的角色"),


    TALKWITHNPC("talkwithnpc",166,"和NPC谈话"),


    SHOWBAG("showbag",159,"展示角色背包"),

    PACKBAG("packbag",168,"整理背包"),


    SHOW_EQUIPMENT_BAR("show_equ_bar",160,"展示装备栏"),


    WEAREQUIP("wearequip",161,"穿上装备"),


    USEITEM("useitem",162,"使用道具"),


    REMOVE_EQUIP("removeequip",163,"卸下武器，放回背包"),


    USESKILLS("useskills",164,"使用技能"),


    ATTACKMONSTER("useskillattackmonster",165,"攻击怪物"),

    SHOWTASK("showtask",167,"展示所有的任务"),

    SHOWACCEPTINGTASKANDPROGRESS("showacceptingtaskandprogress",168,"展示任务进度"),

    ACCEPTTASK("accepttask",169,"接受任务"),

    FINISHTASK("finishtask",170,"完成任务"),

    GIVEUPTASK("giveuptask",171,"放弃任务"),

    ADDFRIEND("addfriend",180,"添加好友"),

    SHOWFRIENDLIST("showfriendlist",181,"显示好友列表"),


    PRIVATECHAT("privatechat",190,"和玩家私聊"),

    PUBLICCHAT("publicchat",191,"公共频道聊天"),

    BUGGOOD("buygood",200,"在商店买货物"),

    SHOWGOODSONSHOP("showgoodsonshop",201,"展示商店中的正在销售的货物"),

    CREATEGUILD("createguild",210,"创建公会"),

    JIONGUILD("joinguild",211,"加入公会"),

    LEAVEGUILD("leaveguild",212,"离开工会"),

    SHOWGAMECOPY("showgamecopy",220,"展示各个场景有的副本"),

    ENTERGAMECOPY("entergamecopy",221,"进入副本"),

    ENTERTEAMGAMECOPY("enterteamgamecopy",225,"团队进入副本"),

    EXITGAMECOPY("exitgamecopy",222,"退出副本"),

    SENDMAIL("sendmail",230,"发送邮件"),

    RECEIVEMAIL("receivemail",231,"接受邮件"),

    MAILSLIST("mailslist",232,"查看邮件列表"),

    INVITEPLAYERJOINTEAM("inviteplayerjointeam",240,"邀请玩家加入队伍"),

    JOINTEAM("jointeam",241,"申请加入队伍"),

    EXITTEAM("exitteam",242,"离开队伍"),

    AGREEPLAYERJOINTEAM("agreeplayerjointeam",243,"同意玩家加入队伍"),

    REFUSEPLAYERJOINTEAM("refuseplayerjointeam",245,"不同意玩家加入队伍"),

    REFUSEJOINTEAM("refusejoinplayer",247,"拒绝加入玩家队伍"),

    GETOUTPLAYERTEAM("getoutplayer",246,"将玩家踢出队伍,队长有资格"),

    SHOWTEAM("showteam",248,"展示队伍信息"),



    /**
     * 玩家注册登录
     * */
    LOGIN("login",153,"用户登录");

    private  String command;

    private Integer orderId;

    /** 说明 **/
    private String explain;

    Orders(String order, Integer orderId, String explain) {
        this.command= order;
        this.orderId = orderId;
        this.explain = explain;
    }



    public static Orders findByOrder(String order){

        for (Orders orderEnum : Orders.values()) {
            if(orderEnum.command.equals(order)) {
                return orderEnum;
            }
        }

        return ErrOrder;

    }

    //根据命令查询命令id

    public static Integer findIdByOrder(String order){
        for(Orders orderEnum : Orders.values()){
            if(orderEnum.equals(order)){
                return orderEnum.orderId;
            }
        }
        return -1;
    }

    //根据id来查询
    public static Orders findOrderById(Integer orderId){
        for(Orders orderEnum : Orders.values()){
            if(orderEnum.orderId.equals(orderId)){
                return orderEnum;
            }
        }
        return ErrOrder;
    }


    public String getCommand(){
        return  command;
    }

    public String getExplain(){
        return explain;
    }
    public Integer getOrderId(){
        return orderId;
    }


}
