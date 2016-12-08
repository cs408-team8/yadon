package com.team8.cs408.yadon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.team8.cs408.yadonDataBase.MyApplication;

public class AlarmActivity extends AppCompatActivity {
    Intent inputIntent;
    String groupName;
    TimePicker startPointAlarmPicker;
    Spinner periodAlarmSpinner;
    int alarmStart, alarmPeriod;
    private Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().setTitle("그룹알람설정");

        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");



        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);   //get columns whose groupName att is set to groupName
        mCursor.moveToNext();
        startPointAlarmPicker = (TimePicker) findViewById(R.id.start_alarm);
        startPointAlarmPicker.setHour(mCursor.getInt(mCursor.getColumnIndex("alarmStart"))/60);
        startPointAlarmPicker.setMinute(mCursor.getInt(mCursor.getColumnIndex("alarmStart"))%60);

        periodAlarmSpinner = (Spinner) findViewById(R.id.period_alarm);
        ArrayAdapter periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.period, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        periodAlarmSpinner.setAdapter(periodAdapter);

        alarmPeriod = mCursor.getInt(mCursor.getColumnIndex("alarmPeriod"));
        switch(alarmPeriod){
            case 6:
                periodAlarmSpinner.setSelection(0);
                break;
            case 12:
                periodAlarmSpinner.setSelection(1);
                break;
            case 24:
                periodAlarmSpinner.setSelection(2);
                break;
            case 48:
                periodAlarmSpinner.setSelection(3);
                break;
            default:
                periodAlarmSpinner.setSelection(1);
                break;
        }

        mCursor.close();
        periodAlarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(parent.getItemAtPosition(position).toString()){
                    case "6시간":
                        alarmPeriod = 6;
                        break;
                    case "12시간":
                        alarmPeriod = 12;
                        break;
                    case "1일":
                        alarmPeriod = 24;
                        break;
                    case "2일":
                        alarmPeriod = 48;
                        break;
                    default:
                        alarmPeriod = 24;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alarmPeriod = 24;
            }
        });

        ImageButton confirmAlarm = (ImageButton) findViewById(R.id.confirm_alarm);
        confirmAlarm.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroupInfoActivity.class);
                intent.putExtra("groupName", groupName);

                alarmStart = startPointAlarmPicker.getHour() * 60 + startPointAlarmPicker.getMinute();

                MyApplication.mDbOpenHelper.updateTheColumn_alarm(groupName, alarmStart, alarmPeriod);
                startActivity(intent);
                finish();
            }
        });

    }
}
