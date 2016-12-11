package com.team8.cs408.yadon;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.team8.cs408.yadonDataBase.DbOpenHelper;
import com.team8.cs408.yadonDataBase.MyApplication;
import com.team8.cs408.yadonDraw.GraphView;


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
                Cursor mCursor = MyApplication.mDbOpenHelper.getAllColumnsUserInfo();
                int i = 0;
                while(mCursor.moveToNext()){
                    i++;
                }
                Log.d("getView Position!! ", "" + i);
                if(i==0){
                    Intent intent = new Intent(LoadingActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 500);
    }
}
