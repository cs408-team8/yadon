package com.team8.cs408.yadon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
    int memberDebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdebt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputIntent = getIntent();
        String groupName = inputIntent.getStringExtra("groupName");
        ArrayList<String> memberNames = inputIntent.getStringArrayListExtra("memberNames");
        memberDebt = inputIntent.getIntExtra("memberDebt", 0);
        getSupportActionBar().setTitle(groupName);

        adapter = new SetDebtMemberListViewAdapter();
        listView = (ListView) findViewById(R.id.listview_member);
        listView.setAdapter(adapter);
        for (int i = 0; i < memberNames.size(); i++) {
            adapter.addItem(memberNames.get(i), memberDebt);
        }

        ImageButton confirm = (ImageButton) findViewById(R.id.setdebt_confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
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
