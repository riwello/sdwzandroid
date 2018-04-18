package com.zyf.sdwzandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zyf.sdwzandroid.App;


/**
 * Created by Administrator on 2016/6/4.
 * momo写的sp的工具类
 */
public class SPUtils {


    public static final String filenName = "AntSPUtil";

    public static final String TOKEN = "selectionTag";
    public static final String USER_NAME = "username";
    public static final String PWD = "pwd";
    public static final String CITY ="city";

    private static Context getApplicationContext() {
        return App.getInstance().getApplicationContext();
    }


    /**
     * 储存String
     */
    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * 获取String
     */
    public static String getString(String key, String defaulValue) {
        return getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).getString(key, defaulValue);
    }

    /**
     * 储存int数据
     */
    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取int
     */
    public static int getInt(String key, int defaulValue) {
        return getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).getInt(key, defaulValue);
    }

    /**
     * 储存boolean数据
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取boolean
     */
    public static boolean getBoolean(String key, boolean defaulValue) {
        return getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).getBoolean(key, defaulValue);

    }

    /**
     * 储存long数据
     */
    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取long
     */
    public static long getLong(String key, long defaulValue) {
        return getApplicationContext().getSharedPreferences(filenName, Context.MODE_PRIVATE).getLong(key, defaulValue);

    }


}
