package com.example.demoserver.common.MsgEntity;

import com.example.demoserver.common.proto.MsgProto;
import lombok.Data;

@Data
public class Msg {

    private int id;

    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString(){
        return content+id;
    }

    public String getContent() {
        return content;
    }

    public int getId(){
        return id;
    }


    //返回Proto格式
    public MsgProto.Msg toProto(){
        return MsgProto.Msg.newBuilder()
                .setContent(content)
                .setMgsId(id)
                .build();
    }
}
