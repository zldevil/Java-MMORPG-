package com.example.demoserver.server.net.dispatch;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
@Component
public class CmdExecutor {

    /** 业务处理的工作方法 */
    private Method method;
    /** 传递给工作方法的相关参数 */
    private Parameter[] params;
    /** 控制器实例 */
    private Object handler;

    public static CmdExecutor valueOf(Method method, Parameter[] params, Object handler) {

        CmdExecutor executor = new CmdExecutor();
        executor.method = method;
        executor.params = params;
        executor.handler = handler;

        return executor;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter[] getParams() {
        return params;
    }

    public Object getHandler() {
        return handler;
    }

}


