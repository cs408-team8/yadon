package com.team8.cs408.yadonDataBase;

import android.app.Application;


public class MyApplication extends Application {
    public static DbOpenHelper mDbOpenHelper;
    @Override
    public void onCreate(){
        super.onCreate();
    }
}
