package com.example.demoserver.game.roleproperty.model;

public class RoleProperty {

    private Integer id;

    private String propertyName;

    private Long propertyValue;

    //？？？
    private  Integer type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Long propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
