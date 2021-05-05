package com.example.plugindemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.mypluginlibrary.PluginItem;
import com.example.mypluginlibrary.PluginManager;


public class App extends Application {
    private static Application mContext;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.init(this);
        mContext = this;
    }

    public static Application getContext(){
       return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("hostplugin", "app oncreate");

        for(PluginItem pluginItem: PluginManager.plugins) {
            try {
                Class clazz = PluginManager.pluginClassLoader.loadClass(pluginItem.applicationName);
                Application application = (Application)clazz.newInstance();
                application.onCreate();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

