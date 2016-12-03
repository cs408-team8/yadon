package com.team8.cs408.yadon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SetDebtActivity extends AppCompatActivity {
    Intent inputIntent;
    private ListView listView;
    SetDebtMemberListViewAdapter adapter;
    ArrayList<String> memberNames;
    ArrayList<Integer> memberDebts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdebt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputIntent = getIntent();
        String groupName = inputIntent.getStringExtra("groupName");
        memberNames = inputIntent.getStringArrayListExtra("memberNames");
        memberDebts = inputIntent.getIntegerArrayListExtra("memberDebts");
        getSupportActionBar().setTitle(groupName);

        adapter = new SetDebtMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        for (int i = 0; i < memberNames.size(); i++) {
            adapter.addItem(memberNames.get(i), memberDebts.get(i));
        }
        ImageButton confirm = (ImageButton) findViewById(R.id.setdebt_confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                adapter.notifyDataSetChanged();
                for (int i = 0; i < listView.getChildCount(); i++) {
                    EditText memberDebtEditText = (EditText) listView.getChildAt(i).findViewById(R.id.item_setmemberdebt);
                    if (memberDebtEditText.getText().toString().length() > 0) {
                        memberDebts.set(i, Integer.parseInt(memberDebtEditText.getText().toString()));
                    } else {
                        memberDebts.set(i, 0);
                    }
                }
                for(int i=0;i<adapter.getCount();i++){
                    ((SetDebtMemberListViewItem) adapter.getItem(i)).setMemberDebt(memberDebts.get(i));
                }
                adapter.notifyDataSetChanged();
                intent.putIntegerArrayListExtra("memberDebts", memberDebts);
                setResult(RESULT_OK, intent);
                finish();                   // go to onActivityResult in HomeActivity.java
            }
        });
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
