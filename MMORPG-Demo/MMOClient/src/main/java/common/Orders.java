package common;

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


    SHOW_EQUIPMENT_BAR("show_equ_bar",160,"展示装备栏"),


    WEAREQUIP("equip",161,"穿上装备"),


    USEGOODS("usegoods",162,"使用道具"),


    REMOVE_EQUIP("removeequip",163,"卸下武器，放回背包"),


    USESKILLS("useskills",164,"使用技能"),


    ATTACK("attack",165,"攻击怪物"),

    SHOWTASK("showtask",167,"展示可领取的任务"),



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
