package com.app2.pms;

import com.app2.pms.common.CrashHandler;

import android.app.Application;

public class ManagerApplication extends Application {

    public static ManagerApplication mApplication = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
        mApplication = this;
    }

    public static ManagerApplication getApplication() {
        return mApplication;
    }
}
