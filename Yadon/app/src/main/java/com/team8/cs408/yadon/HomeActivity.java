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
        updateDBGroupState();
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
            if (mCursor.getInt(mCursor.getColumnIndex("debt")) <= 0) {         //if debt >0, the person have not paid.
                groupMemberRepaidList.set(indexCount - 1,
                        groupMemberRepaidList.get(indexCount - 1) + 1);     //last index's value ++
            }
            groupMemberTotalList.set(indexCount - 1,
                    groupMemberTotalList.get(indexCount - 1) + 1);

        }
        for (int i = 0; i < groupNameList.size(); i++) {
            adapter.addItem(groupNameList.get(i), groupMemberTotalList.get(i), groupMemberRepaidList.get(i));
        }
        adapter.notifyDataSetChanged();
        mCursor.close();
    }

    public void updateDBGroupState() {
        mCursor = MyApplication.mDbOpenHelper.getAllColumns();
        String tempGroupName = "";
        ArrayList<String> groupNameList = new ArrayList<String>();
        int tempDebt = 0;
        while (mCursor.moveToNext()) {
            /*
            Log.d("In HomeActivity : ",
                    " groupName : " + mCursor.getString(mCursor.getColumnIndex("groupName")) +
                            " name : " + mCursor.getString(mCursor.getColumnIndex("name")) +
                            " initDebt : " + mCursor.getString(mCursor.getColumnIndex("initDebt")) +
                            " debt : " + mCursor.getString(mCursor.getColumnIndex("debt")));
                            */
            tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
            if (!groupNameList.contains(tempGroupName)) {
                groupNameList.add(tempGroupName);
            }
        }
        mCursor.close();
        for (int i = 0; i < groupNameList.size(); i++) {
            int groupTotalDebt = 0;
            int debtSetup = 0;
            mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupNameList.get(i));
            while (mCursor.moveToNext()) {
                groupTotalDebt += mCursor.getInt(mCursor.getColumnIndex("debt"));
                debtSetup = mCursor.getInt(mCursor.getColumnIndex("debtSetup"));
            }
            mCursor.close();
            if (debtSetup != 0 && groupTotalDebt == 0) {
                MyApplication.mDbOpenHelper.updateColumns_groupState_collectionCompleted(groupNameList.get(i), 1);  // completed
            }
        }
    }

    //Group member is selected. Should handle the information.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, "유저정보");
        //Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
        // 이름 계좌 은행(콤보박스)

        menu.add(0, 2, 2, "설정");


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, UserSettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            case 3:
                return true;
        }
        return false;
    }


    /**
     * Show the contacts in the ListView. This is for initial permission for read_contacts.
     * Without this, we cannot do anything.
     * <uses-permission android:name="android.permission.SEND_SMS" />
     * <uses-permission android:name="android.permission.RECEIVE_SMS" />
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
        }
    }
/*
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
*/
}
