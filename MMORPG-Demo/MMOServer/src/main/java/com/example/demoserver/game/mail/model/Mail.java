package com.example.demoserver.game.mail.model;

import lombok.Data;

@Data
public class Mail {

    private Integer id;

    private String content;

    private Integer senderId;

    private Integer receiverId;

    private Boolean hasRead;

    private Integer money;

    private String attachment;



}
