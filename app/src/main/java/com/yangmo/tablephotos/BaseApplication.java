package com.yangmo.tablephotos;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Author: yankeliang
 * Date: 2018/7/31 11:01
 * Description:
 */

public class BaseApplication extends Application {

    public static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Bugly.init(getApplicationContext(), "08094ccd80", false);
    }

    public static BaseApplication getBaseApplication() {
        return instance;
    }

}
