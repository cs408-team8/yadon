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
    public final int SetDebtActivityCode = 3;
    ArrayList<String> memberNames;
    ArrayList<String> memberPhones;
    String groupName;
    ArrayList<Integer> memberDebts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");
        memberDebts = new ArrayList<Integer>();
        getSupportActionBar().setTitle(groupName);
        adapter = new GroupMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        updateGroupMemberListView();

        ImageButton setDebtButton = (ImageButton) findViewById(R.id.setDebtButton);
        setDebtButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfoActivity.this, SetDebtActivity.class);
                intent.putExtra("groupName", groupName);
                intent.putStringArrayListExtra("memberNames", memberNames);
                intent.putIntegerArrayListExtra("memberDebts", memberDebts);
                startActivityForResult(intent, SetDebtActivityCode);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SetDebtActivityCode) {
                memberDebts = data.getIntegerArrayListExtra("memberDebts");
                for (int i = 0; i < memberDebts.size(); i++) {
                    ((GroupMemberListViewItem) adapter.getItem(i)).setMemberDebt(memberDebts.get(i));
                }
                adapter.notifyDataSetChanged();     //update the listview
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateGroupMemberListView() {
        adapter.clear();
        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);
        Log.d("number of columns : ", "" + mCursor.getCount());
        while (mCursor.moveToNext()) {
            memberNames.add(mCursor.getString(mCursor.getColumnIndex("name")));
            memberPhones.add(mCursor.getString(mCursor.getColumnIndex("phoneNumber")));
            memberDebts.add(mCursor.getInt(mCursor.getColumnIndex("debts")));
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
