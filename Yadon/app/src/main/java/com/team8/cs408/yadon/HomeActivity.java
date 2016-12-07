package com.team8.cs408.yadon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    private Cursor mCursor;
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
        updateGroupListView();
        addGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MakeGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //If User touches a group, goes to GroupInfoActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListViewItem item = (GroupListViewItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), GroupInfoActivity.class);

                intent.putExtra("groupName", item.getGroupName());
                /*
                intent.putStringArrayListExtra("memberNames", item.getMemberNames());
                intent.putStringArrayListExtra("memberPhones", item.getMemberPhones());
                */
                startActivity(intent);
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListViewItem item = (GroupListViewItem) parent.getItemAtPosition(position);
                MyApplication.mDbOpenHelper.deleteColumn(item.getGroupName());
                updateGroupListView();
                return false;
            }
        });
    }

    private void updateGroupListView() {
        adapter.clear();
        mCursor = MyApplication.mDbOpenHelper.getAllColumns();
        Log.d("number of columns : ", "" + mCursor.getCount());
        ArrayList<String> groupNameList = new ArrayList<String>();
        String tempGroupName;
        while (mCursor.moveToNext()) {
            tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
            if (!groupNameList.contains(tempGroupName)) {
                groupNameList.add(tempGroupName);
            }
        }
        for (int i = 0; i < groupNameList.size(); i++) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.graph_sample), groupNameList.get(i));
        }
        adapter.notifyDataSetChanged();
        mCursor.close();
    }

    //Group member is selected. Should handle the information.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, "유저정보");
        menu.add(0, 2, 2, "설정");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

}
