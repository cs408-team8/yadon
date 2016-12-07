package com.team8.cs408.yadon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.util.ArrayList;


public class GroupInfoActivity extends AppCompatActivity {
    Intent inputIntent;
    private ListView listView;
    private Cursor mCursor;
    GroupMemberListViewAdapter adapter;
    ArrayList<String> memberNames;
    ArrayList<String> memberPhones;
    String groupName;
    ArrayList<Integer> memberDebts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button

        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");

        memberNames = new ArrayList<String>();
        memberPhones = new ArrayList<String>();
        memberDebts = new ArrayList<Integer>();

        getSupportActionBar().setTitle(groupName);

        adapter = new GroupMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        updateGroupMemberListView();

        ImageButton setDebtButton = (ImageButton) findViewById(R.id.setDebtButton); //set debt button
        setDebtButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SetDebtActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
                finish();
            }
        });
    }
                    //((GroupMemberListViewItem) adapter.getItem(i)).setMemberDebt(memberDebts.get(i));


    private void updateGroupMemberListView() {
        adapter.clear();
        memberNames.clear();
        memberPhones.clear();
        memberDebts.clear();
        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);   //get columns whose groupName att is set to groupName
        while (mCursor.moveToNext()) {
            memberNames.add(mCursor.getString(mCursor.getColumnIndex("name")));
            memberPhones.add(mCursor.getString(mCursor.getColumnIndex("phoneNumber")));
            memberDebts.add(mCursor.getInt(mCursor.getColumnIndex("debt")));
        }
        for (int i = 0; i < memberNames.size(); i++) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.member_ok), memberNames.get(i), memberPhones.get(i), memberDebts.get(i));
        }
        adapter.notifyDataSetChanged();
        mCursor.close();
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(GroupInfoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Phone back button
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(GroupInfoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
