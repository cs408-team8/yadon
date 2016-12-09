package com.team8.cs408.yadon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by newwhite on 2016. 12. 9..
 */

public class UserSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        getSupportActionBar().setTitle("그룹");

        ListView userset = (ListView)findViewById(R.id.usersetting_list);
        UserSettingViewAdapter adapter = new UserSettingViewAdapter();
        adapter.addItem("이름","김민석");
        adapter.addItem("은행","우리은행");
        adapter.addItem("계좌번호","1002-681-242102");
        userset.setAdapter(adapter);


    }

}



// 상세 메세지 설정
// 유저정보 변경 ->
// 알람성정 돈 다 받을때 push 알림 받을까 안받을까
// 버전정보