package com.example.plugindemo.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.mypluginlibrary.utils.RefInvoke;

public class AppEvilInstrumentation extends Instrumentation {

    private static final String TAG = "AppEvilInstrumentation";

    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;

    public AppEvilInstrumentation(Instrumentation base) {
        mBase = base;
    }

    @Override
        public Activity newActivity(ClassLoader cl, String className,
                                Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {

        Log.d(TAG, "包建强到此一游!");

        // 把替身恢复成真身
        Intent rawIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        if(rawIntent == null) {
            return mBase.newActivity(cl, className, intent);
        }
        String newClassName = rawIntent.getComponent().getClassName();
        return mBase.newActivity(cl, newClassName, rawIntent);

    }




    public void callActivityOnCreate(Activity activity, Bundle bundle) {

        Log.d(TAG, "到此一游!");

        Class[] p1 = {Activity.class, Bundle.class};
        Object[] v1 = {activity, bundle};
        RefInvoke.invokeInstanceMethod(
                mBase, "callActivityOnCreate", p1, v1);
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        Log.v(TAG, "×××到此一游!");

        // 开始调用原始的方法, 调不调用随你,但是不调用的话, 所有的startActivity都失效了。
        // 由于这个方法是隐藏的,因此需要使用反射调用;首先找到这个方法
        Class[] p1 = {Context.class, IBinder.class,
                IBinder.class, Activity.class,
                Intent.class, int.class, Bundle.class};
        Object[] v1 = {who, contextThread, token, target,
                intent, requestCode, options};
        return (ActivityResult) RefInvoke.invokeInstanceMethod(
                mBase, "execStartActivity", p1, v1);
    }
}