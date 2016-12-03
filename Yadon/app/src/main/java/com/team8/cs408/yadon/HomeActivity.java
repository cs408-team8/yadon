package com.team8.cs408.yadon;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    public final int MakeGroupActivityCode = 0;
    public final int GroupInfoActivityCode = 1;
    GroupListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("그룹");
        ImageButton addGroupButton = (ImageButton) findViewById(R.id.addGroup);
        adapter = new GroupListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_group);
        listView.setAdapter(adapter);
        addGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MakeGroupActivity.class);
                startActivityForResult(intent, MakeGroupActivityCode);
            }
        });

        //If User touches a group, goes to GroupInfoAcitivy
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupInfoListViewItem item = (GroupInfoListViewItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(HomeActivity.this, GroupInfoActivity.class);
                intent.putExtra("groupName", item.getGroupName());
                intent.putStringArrayListExtra("memberNames", item.getMemberNames());
                intent.putStringArrayListExtra("memberPhones", item.getMemberPhones());
                startActivityForResult(intent, GroupInfoActivityCode);
            }
        });
    }

    //Group member is selected. Should handle the information.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == MakeGroupActivityCode) {
                ArrayList<String> checkedNames = data.getStringArrayListExtra("checkedNames");
                ArrayList<String> checkedPhones = data.getStringArrayListExtra("checkedPhones");
                String groupName = data.getStringExtra("groupName");
                adapter.addItem(ContextCompat.getDrawable(this, R.drawable.graph_sample), groupName, checkedNames, checkedPhones);
            } else if (requestCode == GroupInfoActivityCode) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, "유저정보");
        menu.add(0, 2, 2, "설정");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item != null){
            Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

}
