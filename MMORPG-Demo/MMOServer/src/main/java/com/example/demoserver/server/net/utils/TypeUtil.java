package com.example.demoserver.server.net.utils;

import java.lang.reflect.Field;

public class TypeUtil {

    public static Object returnCorrectType(Object object, Field field){
        if(field.getType().equals(Long.class)){

            Integer tmp=(Integer) object;
            Long result=tmp.longValue();

            return result;

        }

        if(field.getType().equals(Float.class)){
            Integer tmp=(Integer) object;
            Float result=tmp.floatValue();

            return result;
        }

       return object;
    }

}
