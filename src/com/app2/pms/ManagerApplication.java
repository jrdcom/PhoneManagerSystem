package com.app2.pms;

import android.app.Application;

public class ManagerApplication extends Application {

    public static ManagerApplication mApplication = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mApplication = this;
    }

    public static ManagerApplication getApplication() {
        return mApplication;
    }
}
