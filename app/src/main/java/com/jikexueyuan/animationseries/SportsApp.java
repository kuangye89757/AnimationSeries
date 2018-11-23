package com.jikexueyuan.animationseries;

import android.app.Application;
import android.content.Context;

public class SportsApp extends Application {

    private static SportsApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static Context getContext() {
        return mApp;
    }
}
