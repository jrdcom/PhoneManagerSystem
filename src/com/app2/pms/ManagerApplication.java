package com.app2.pms;

import com.app2.pms.common.CrashHandler;
import com.app2.pms.debug.app.DebugService;

import android.app.Application;
import android.content.Intent;

public class ManagerApplication extends Application {

    public static ManagerApplication mApplication = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
        mApplication = this;
        startService(new Intent(this, DebugService.class));
    }

    public static ManagerApplication getApplication() {
        return mApplication;
    }
}
