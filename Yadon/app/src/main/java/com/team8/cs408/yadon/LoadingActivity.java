package com.team8.cs408.yadon;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.team8.cs408.yadonDataBase.DbOpenHelper;
import com.team8.cs408.yadonDataBase.MyApplication;


public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        MyApplication.mDbOpenHelper = new DbOpenHelper(this);
        MyApplication.mDbOpenHelper.open();
        //MyApplication.mDbOpenHelper.flush();
        Handler handler = new Handler(){
            public void handleMessage(Message msg){
                Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 500);
    }
}
