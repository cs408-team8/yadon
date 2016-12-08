package com.team8.cs408.yadon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
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
    GroupListViewAdapter adapter;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("그룹");

        showContacts();     //for the permission of read_contacts

        ImageButton addGroupButton = (ImageButton) findViewById(R.id.addGroup);

        adapter = new GroupListViewAdapter(this);
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

                intent.putExtra("groupName", item.getGroupName());  //group name is needed for groupinfoactivity.

                startActivity(intent);
                finish();
            }
        });
        //If long touch, group information goes away
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
    //Update the adapter to show the current data.
    private void updateGroupListView() {
        adapter.clear();
        mCursor = MyApplication.mDbOpenHelper.getAllColumns();
        Log.d("number of columns : ", "" + mCursor.getCount());
        int indexCount = 0;
        ArrayList<String> groupNameList = new ArrayList<String>();
        ArrayList<Integer> groupMemberTotalList = new ArrayList<Integer>();         // number of the group members
        ArrayList<Integer> groupMemberRepaidList = new ArrayList<Integer>();        // number of the group members who repaid
        String tempGroupName;
        while (mCursor.moveToNext()) {
            tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
            if (!groupNameList.contains(tempGroupName)) {
                groupNameList.add(tempGroupName);
                groupMemberTotalList.add(0);
                groupMemberRepaidList.add(0);
                indexCount++;
            }
            if(mCursor.getInt(mCursor.getColumnIndex("debt")) <= 0){         //if debt >0, the person have not paid.
                groupMemberRepaidList.set(indexCount-1,
                        groupMemberRepaidList.get(indexCount-1) + 1);     //last index's value ++
            }
            groupMemberTotalList.set(indexCount-1,
                    groupMemberTotalList.get(indexCount-1) + 1);

        }
        Log.d("indexCount ! ", ""+indexCount);
        for (int i = 0; i < groupNameList.size(); i++) {
            //adapter.addItem(ContextCompat.getDrawable(this, R.drawable.graph_sample), groupNameList.get(i));
            adapter.addItem(groupNameList.get(i), groupMemberTotalList.get(i), groupMemberRepaidList.get(i));
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


    /**
     * Show the contacts in the ListView. This is for initial permission for read_contacts.
     * Without this, we cannot do anything.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
