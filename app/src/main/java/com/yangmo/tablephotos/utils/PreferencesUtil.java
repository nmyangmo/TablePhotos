package com.yangmo.tablephotos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yangmo.tablephotos.BaseApplication;

import java.util.List;


public class PreferencesUtil {
    private static final String SP_NAME = "PhotosTables";
    private static SharedPreferences sharedPreferences = BaseApplication.getBaseApplication().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sharedPreferences.edit();
    private static String PHOTO_LIST="photoList";


    public static void setPhotoList(String loginName) {
        setString(PHOTO_LIST, loginName);
    }

    public static List<String> getPhotoList() {
        List<String> list=null;
        String uriString=getString(PHOTO_LIST, "");
        if (!uriString.isEmpty()){
            list= JSON.parseArray(uriString,String.class);
        }
        return list;
    }
    /*********************************统一写入读取方法*************************************/
    public static String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static Float getFloat(String key, Float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void setFloat(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);

    }

    public static void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static void clearData() {
        editor.clear();
        editor.commit();
    }
}
