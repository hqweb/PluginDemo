package com.example.plugin1;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class AppPlu extends Application {
    public static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.v("plugincontext", "app att--"+R.style.Theme_AppCompat);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.v("plugin", "app oncreate");
    }
}
