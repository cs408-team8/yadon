package com.team8.cs408.yadon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

        adapter = new SetDebtMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        updateGroupMemberListView();

        ImageButton confirm = (ImageButton) findViewById(R.id.setdebt_confirmButton);
        confirm.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GroupInfoActivity.class);
                intent.putExtra("groupName", groupName);
                /*
                adapter.notifyDataSetChanged();
                for (int i = 0; i < listView.getChildCount(); i++) {
                    EditText memberDebtEditText = (EditText) listView.getChildAt(i).findViewById(R.id.item_setmemberdebt);
                    if (memberDebtEditText.getText().toString().length() > 0) {
                        memberDebts.set(i, Integer.parseInt(memberDebtEditText.getText().toString()));
                    } else {
                        memberDebts.set(i, 0);
                    }
                }*/
                updateGroupMemberListView();
                startActivity(intent);
                finish();                   // go to onActivityResult in HomeActivity.java
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
