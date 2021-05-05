package com.example.plugindemo.process;

import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mypluginlibrary.PluginManager;
import com.example.mypluginlibrary.utils.RefInvoke;
import com.example.plugindemo.hook.AMSHookHelper;
import com.example.plugindemo.hook.InstrumentationHook;
import com.example.plugindemo.runtime.DynamicRuntime;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PluginProcessService extends BasePluginProcessService {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PpsBinder(this);
    }

    public static PpsController wrapBinder(IBinder ppsBinder) {
        return new PpsController(ppsBinder);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void startPlugin(){
        ComponentName componentName=new ComponentName(this,"com.example.plugin1.SubActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initPlugin() {
        Class  cll = null;
        try {
            AMSHookHelper.hookAMN();
            InstrumentationHook.attachContext();
            cll = PluginManager.pluginClassLoader.loadClass("com.example.plugin1.utils.ResourcesManager");
            RefInvoke.setStaticFieldObject(cll, "mNowResources", PluginManager.mNowResources);
            DynamicRuntime.hackParentClassLoader(PluginProcessService.class.getClassLoader(), PluginManager.pluginClassLoader);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
