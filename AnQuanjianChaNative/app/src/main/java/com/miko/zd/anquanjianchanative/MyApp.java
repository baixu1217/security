package com.miko.zd.anquanjianchanative;

import android.app.Application;
import android.content.res.Configuration;

import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

public class MyApp extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900058155", true);
        System.out.println("MyApp is called");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Realm.init(this);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}