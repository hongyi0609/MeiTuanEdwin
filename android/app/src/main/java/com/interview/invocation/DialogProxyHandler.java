package com.interview.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Edwin on 2020/9/10.
 *
 * @author Edwin
 */

public class DialogProxyHandler implements InvocationHandler {

    private Object mObject;

    public DialogProxyHandler(Object o) {
        mObject = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("handleDialog".equals(method.getName())) {
            method.invoke(mObject,args);
            return null;
        }
        return method.invoke(mObject,args);
    }
}
