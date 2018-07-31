package com.yangmo.tablephotos;

import android.app.Application;

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
    }

    public static BaseApplication getBaseApplication() {
        return instance;
    }

}
