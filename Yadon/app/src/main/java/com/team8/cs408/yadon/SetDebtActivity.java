package com.team8.cs408.yadon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SetDebtActivity extends AppCompatActivity {
    Intent inputIntent;
    private ListView listView;
    SetDebtMemberListViewAdapter adapter;
    String groupName;
    ArrayList<String> memberNames;
    ArrayList<Integer> memberDebts;
    Cursor mCursor;
    EditText total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdebt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");

        memberNames = new ArrayList<String>();
        memberDebts = new ArrayList<Integer>();

        getSupportActionBar().setTitle(groupName);

        adapter = new SetDebtMemberListViewAdapter(this);
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        updateGroupMemberListView();

        total = (EditText) findViewById(R.id.editText);


        ImageButton confirm = (ImageButton) findViewById(R.id.setdebt_confirmButton);
        confirm.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AlarmActivity.class);
                intent.putExtra("groupName", groupName);
                updateGroupMemberListView();
                startActivity(intent);
                finish();                   // go to onActivityResult in HomeActivity.java
            }
        });


        ImageButton dutch = (ImageButton) findViewById(R.id.setdebt_dutchpayButton);
        dutch.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s;
                if (total.getText().toString().length() <= 0) {
                    s = "0";
                } else {
                    s = total.getText().toString();
                }
                for (int i = 0; i < adapter.getCount(); i++) {
                    SetDebtMemberListViewItem item = (SetDebtMemberListViewItem) adapter.getItem(i);
                    MyApplication.mDbOpenHelper.updateTheColumn_debt(item.getGroupName(),
                            item.getMemberName(), Integer.parseInt(s) / (adapter.getCount() + 1));
                    item.setMemberDebt(Integer.parseInt(s) / (adapter.getCount() + 1));

                }
                updateGroupMemberListView();
            }
        });
    }

    private void updateGroupMemberListView() {
        adapter.clear();
        memberNames.clear();
        memberDebts.clear();
        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);   //get columns whose groupName att is set to groupName
        while (mCursor.moveToNext()) {
            memberNames.add(mCursor.getString(mCursor.getColumnIndex("name")));
            memberDebts.add(mCursor.getInt(mCursor.getColumnIndex("debt")));
        }
        for (int i = 0; i < memberNames.size(); i++) {
            adapter.addItem(groupName, memberNames.get(i), memberDebts.get(i));
        }
        adapter.notifyDataSetChanged();
        mCursor.close();
    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SetDebtActivity.this, GroupInfoActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Phone back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetDebtActivity.this, GroupInfoActivity.class);
        intent.putExtra("groupName", groupName);
        startActivity(intent);
        finish();
    }
}