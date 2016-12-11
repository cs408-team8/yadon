package com.team8.cs408.yadon;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
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
            Log.d("total repaid : ", "total : "+groupMemberTotalList.get(i)+", repaid : "+groupMemberRepaidList.get(i));
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

            Log.d("In HomeActivity : ",
                    " groupName : " + mCursor.getString(mCursor.getColumnIndex("groupName")) +
                            " debtSetup : " + mCursor.getString(mCursor.getColumnIndex("debtSetup")) +
                            " collectionCompleted : " + mCursor.getString(mCursor.getColumnIndex("collectionCompleted")) +
                            " name : " + mCursor.getString(mCursor.getColumnIndex("name")));

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

        menu.add(0,3,3,"장부엑셀파일생성");


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Cursor mCursor = MyApplication.mDbOpenHelper.getAllColumnsUserInfo();
                mCursor.moveToNext();

                Toast.makeText(this, "이름"+mCursor.getString(mCursor.getColumnIndex("userName"))+"\n은행"+mCursor.getString(mCursor.getColumnIndex("userBank"))+"\n계좌번호"+mCursor.getString(mCursor.getColumnIndex("userAccount"))+"\n문자예시\n"+mCursor.getString(mCursor.getColumnIndex("messageSample")), Toast.LENGTH_LONG).show();
                return true;
            case 2:
                Intent intent = new Intent(HomeActivity.this, UserSettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            case 3:
                String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
                File dir = new File(sdpath+"/Yadon");
                dir.mkdir();
                File file = new File(sdpath+"/Yadon/AccountBook_Yadon.csv");
                try{
                    FileOutputStream fos = new FileOutputStream(file);
                    Cursor cursor = MyApplication.mDbOpenHelper.getAllColumsForAccountBook();
                    int subtotal = 0;
                    int total = 0;
                    int subdebt = 0;
                    int totaldebt = 0;
                    String str = "그룹,날짜,입금자,금액,미수납액\n";
                    fos.write(str.getBytes());
                    String group = "";
                    while(cursor.moveToNext()) {
                        Log.d("In home activity, ", cursor.getString(cursor.getColumnIndex("groupName")));
                        if(group.equals("")){
                            str = cursor.getString(cursor.getColumnIndex("groupName"))+","+cursor.getString(cursor.getColumnIndex("creationDate"))+","+cursor.getString(cursor.getColumnIndex("name"))
                                    +","+cursor.getString(cursor.getColumnIndex("initDebt"))+","+cursor.getString(cursor.getColumnIndex("debt"))+"\n";
                            fos.write(str.getBytes());
                            group=cursor.getString(cursor.getColumnIndex("groupName"));
                            subdebt=cursor.getInt(cursor.getColumnIndex("debt"));
                            subtotal=cursor.getInt(cursor.getColumnIndex("initDebt"));
                        }else if(!group.equals(cursor.getString(cursor.getColumnIndex("groupName")))){
                            total+=subtotal;
                            totaldebt+=subdebt;
                            str = ",,금액계,"+String.valueOf(subtotal)+",\n";
                            fos.write(str.getBytes());
                            str = ",,미수납액계,,"+String.valueOf(subdebt)+",\n";
                            fos.write(str.getBytes());
                            str = cursor.getString(cursor.getColumnIndex("groupName"))+","+cursor.getString(cursor.getColumnIndex("creationDate"))+","+cursor.getString(cursor.getColumnIndex("name"))
                                    +","+cursor.getString(cursor.getColumnIndex("initDebt"))+","+cursor.getString(cursor.getColumnIndex("debt"))+"\n";
                            fos.write(str.getBytes());
                            group=cursor.getString(cursor.getColumnIndex("groupName"));
                            subdebt=cursor.getInt(cursor.getColumnIndex("debt"));
                            subtotal=cursor.getInt(cursor.getColumnIndex("initDebt"));

                        }
                        else{
                            str = ",,"+cursor.getString(cursor.getColumnIndex("name"))+","+cursor.getString(cursor.getColumnIndex("initDebt"))+","+cursor.getString(cursor.getColumnIndex("debt"))+"\n";
                            fos.write(str.getBytes());
                            subdebt+=cursor.getInt(cursor.getColumnIndex("debt"));
                            subtotal+=cursor.getInt(cursor.getColumnIndex("initDebt"));
                        }

                    }
                    total+=subtotal;
                    totaldebt+=subdebt;
                    str = ",,금액계,"+String.valueOf(subtotal)+",\n";
                    fos.write(str.getBytes());
                    str = ",,미수납액계,,"+String.valueOf(subdebt)+",\n";
                    fos.write(str.getBytes());
                    str = ",,금액총계,"+String.valueOf(total)+",\n";
                    fos.write(str.getBytes());
                    str = ",,미수납액총계,,"+String.valueOf(totaldebt)+",\n";
                    fos.write(str.getBytes());
                    fos.close();
                }
                catch(Exception e){;}

                Toast.makeText(this,"내파일/Yadon 폴더에 장부가 생성되었습니다.",Toast.LENGTH_SHORT).show();
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
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
