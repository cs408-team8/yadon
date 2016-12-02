package com.team8.cs408.yadon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;


public class GroupInfoActivity extends AppCompatActivity {
    Intent inputIntent;
    private ListView listView;
    GroupMemberListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);
        inputIntent = getIntent();
        ArrayList<String> memberNames = inputIntent.getStringArrayListExtra("memberNames");
        ArrayList<String> memberPhones = inputIntent.getStringArrayListExtra("memberPhones");

        adapter = new GroupMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        for (int i = 0; i < memberNames.size(); i++) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.member_ok), memberNames.get(i), memberPhones.get(i), 500);
        }
    }
}
