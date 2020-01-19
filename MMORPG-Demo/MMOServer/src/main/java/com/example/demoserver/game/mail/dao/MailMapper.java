package com.example.demoserver.game.mail.dao;

import com.example.demoserver.game.mail.model.Mail;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MailMapper {


    int insertMail(Mail mail);

    List<Mail> selectMailsByReceivePlayerId(Integer receiveId);

    Mail selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKey(Mail mail);


      // TODO 使用线程池进行异步持久化;
}
