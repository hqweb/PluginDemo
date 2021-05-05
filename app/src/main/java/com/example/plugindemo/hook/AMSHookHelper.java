package com.example.plugindemo.hook;

import android.util.Log;

import com.example.mypluginlibrary.utils.RefInvoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AMSHookHelper {
    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static void hookAMN() throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {
        Log.v("hookamnn",  Class.forName("android.app.ActivityTaskManager").getClassLoader()+"");

        //获取AMN的gDefault单例gDefault, gDefault是final静态的
        Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityTaskManager", "IActivityTaskManagerSingleton");

        // gDefault是一个 android.util.Singleton<T>对象; 取出这个单例中的mInstance字段
      // Object mInstance = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance");

        Method method = gDefault.getClass().getMethod("get", new Class[]{}); //在指定类中获取指定的方法
        Object mInstance = method.invoke(gDefault, null);//result_3为null,该方法不返回结果

       // Object mInstance = RefInvoke.invokeInstanceMethod(gDefault, "get");

        // 创建一个这个对象的代理对象MockClass1, 然后替换这个字段, 让我们的代理对象帮忙干活
        Class<?> classB2Interface = Class.forName("android.app.IActivityTaskManager");
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { classB2Interface },
                new MockClass1(mInstance));

        //把gDefault的mInstance字段, 修改为proxy
        RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }
}

