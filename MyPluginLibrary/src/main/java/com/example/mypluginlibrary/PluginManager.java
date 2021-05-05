package com.example.mypluginlibrary;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.example.mypluginlibrary.utils.DLUtils;
import com.example.mypluginlibrary.utils.RefInvoke;
import com.example.mypluginlibrary.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class PluginManager {
    public final static List<PluginItem> plugins = new ArrayList<PluginItem>();

    public static volatile Resources mNowResources;

    //原始的application中的BaseContext，不能是其他的，否则会内存泄漏
    public static volatile Context mBaseContext;

    public static volatile ClassLoader mBaseClassLoader = null;         //系统原始的ClassLoader
    public static volatile ClassLoader pluginClassLoader = null;         //系统原始的ClassLoader

    public static void initResources(String dexpath1){
        Resources superRes = mBaseContext.getResources();
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod
                    ("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexpath1);
            mNowResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dexOutputDir = mBaseContext.getDir("dex", Context.MODE_PRIVATE);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        pluginClassLoader = new DexClassLoader(dexpath1,
                dexOutputPath, null, mBaseClassLoader.getParent());

    }
    public static void init(Application application) {
        mBaseContext = application.getBaseContext();
        mBaseClassLoader = mBaseContext.getClassLoader();

        try {
            AssetManager assetManager = application.getAssets();
            String[] paths = assetManager.list("");

            ArrayList<String> pluginPaths = new ArrayList<String>();
            for(String path : paths) {
                if(path.endsWith(".apk")) {
                    String apkName = path;

                    Utils.extractAssets(mBaseContext, apkName);

                    PluginItem item = generatePluginItem(apkName);
                    plugins.add(item);

                    pluginPaths.add(item.pluginPath);
                }
            }
            initResources(pluginPaths.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static PluginItem generatePluginItem(String apkName) {
        File file = mBaseContext.getFileStreamPath(apkName);
        PluginItem item = new PluginItem();
        item.pluginPath = file.getAbsolutePath();
        item.packageInfo = DLUtils.getPackageInfo(mBaseContext, item.pluginPath);
        item.applicationName = ApplicationHelper.loadApplication(mBaseContext, file);

        return item;
    }


}