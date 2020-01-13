package com.example.demoserver.server.net.dispatch;


import com.example.demoserver.common.MsgEntity.Msg;
import com.example.demoserver.common.Orders;
import com.example.demoserver.server.net.annoation.Controller;
import com.example.demoserver.server.net.annoation.RequestMapping;
import com.example.demoserver.server.net.utils.BeanNameUtil;
import com.example.demoserver.server.net.utils.ClassScanner;
import com.example.demoserver.server.net.utils.SplitParameters;
import com.example.demoserver.server.net.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


@Slf4j
public class MessageDispatcher {

    private volatile static MessageDispatcher instance;

    private static final Map<String, CmdExecutor> CMD_HANDLERS = new HashMap<>();

    public static MessageDispatcher getInstance() {
        //双重检查锁单例
        if (instance == null) {
            synchronized (MessageDispatcher.class) {
                if (instance == null) {
                    instance = new MessageDispatcher();

                    try {
                        initalize();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    public  MessageDispatcher() {

    }

    public static void initalize() throws IOException, ClassNotFoundException {

                String[] packages={"com.example.demoserver.game"};

                ClassScanner classScanner =new ClassScanner(packages,Controller.class);

                Set<Class<?>> controllers = classScanner.getClassSet();

                for (Class<?> controller: controllers) {

                            try {

                                log.info("初始化"+controller);

                                Object handler= BeanNameUtil.getBean(controller);

                                Method[] methods = controller.getDeclaredMethods();

                                for (Method method:methods) {
                                    RequestMapping mapperAnnotation = method.getAnnotation(RequestMapping.class);
                                    if (mapperAnnotation != null) {

                                        Orders order =mapperAnnotation.getOrder();
                                        String command=order.getCommand();
                                        CmdExecutor cmdExecutor = CMD_HANDLERS.get(command);

                                        if (cmdExecutor != null) {
                                            throw new RuntimeException("处理器重复");
                                        }else {

                                            cmdExecutor = CmdExecutor.valueOf(method, method.getParameters(), handler);

                                            CMD_HANDLERS.put(command,cmdExecutor);

                                        }
                    }
                }
            }catch(Exception e) {
                                e.printStackTrace();
            }
        }
                log.info("初始化完成");

    }

    /**
     * 向线程池分发消息
     * @param
     * @param message
     */

    public void dispatch(ChannelHandlerContext ctx,  Msg message) {

        String[] command  =SplitParameters.split(message);

        CmdExecutor cmdExecutor = CMD_HANDLERS.get(command[0]);

        if (cmdExecutor==null) {

            log.error("请求协议不存在");
            return ;

        }

          Parameter[] params =cmdExecutor.getParams();
          Object controller = cmdExecutor.getHandler();

        try {

             cmdExecutor.getMethod().invoke(controller,new Object[]{ctx, message});

        }catch(Exception e) {

            e.printStackTrace();

            log.error("dispatch message failed");
        }

    }

public static void messageReceived(ChannelHandlerContext ctx, Msg message) throws Exception
        {

        System.err.println("收到消息-->" + message);

        MessageDispatcher.getInstance().dispatch(ctx, message);

        }

}
