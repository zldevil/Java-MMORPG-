package com.example.demoserver.server.net.annoation;

import com.example.demoserver.common.Orders;

import java.lang.annotation.*;

/**
 * 处理请求地址映射的注解
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    Orders getOrder();

}