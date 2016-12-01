package com.team8.cs408.yadon;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;


public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    GroupListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageButton addGroupButton = (ImageButton) findViewById(R.id.addGroup);
        adapter = new GroupListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_group);
        listView.setAdapter(adapter);
        addGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MakeGroupActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupInfoListViewItem item = (GroupInfoListViewItem) parent.getItemAtPosition(position);

            }
        });
    }

    //Group member is selected. Should handle the information.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> checkedNames = data.getStringArrayListExtra("checkedNames");
            ArrayList<String> checkedPhones = data.getStringArrayListExtra("checkedPhones");
            String groupName = data.getStringExtra("groupName");
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.graph_sample), groupName, checkedNames, checkedPhones);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
