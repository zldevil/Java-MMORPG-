package com.example.demoserver.game.equip.model;

public enum EquitmentPart {


    /**
     *
     */
    HADN(0, "手"),
    UPPERBODY(1,"上装"),
    LOWERBODY(2,"下装"),
    SHOUDLER(3,"肩部"),
    BELT(4,"腰带"),
    SHOE(5,"鞋子"),

    NECKLACE(6,"项链"),
    BRACELET(7,"手镯"),
    RING(8,"戒指"),

    PACKAGE(9, "背包"),
    ;


    private String part;
    private Integer code;

    EquitmentPart(Integer code, String part) {
        this.part = part;
        this.code = code;
    }

    public String getPart() {
        return part;
    }

    public Integer getCode() {
        return code;
    }

    public static String getPartByCode(Integer code) {
        for (EquitmentPart par : EquitmentPart.values()) {
            if(par.getCode().equals(code)) {
                return par.getPart();
            }
        }
        return null;
    }
}
