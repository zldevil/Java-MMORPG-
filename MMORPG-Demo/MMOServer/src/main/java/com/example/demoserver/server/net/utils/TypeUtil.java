package com.example.demoserver.server.net.utils;

import java.lang.reflect.Field;

public class TypeUtil {

    public static Object returnCorrectType(Object object, Field field){
        if(field.getType().equals(Long.class)){

            Integer tmp=(Integer) object;
            Long result=tmp.longValue();

            return result;

        }

       return object;
    }

}
