package com.team8.cs408.yadon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.team8.cs408.yadonDataBase.MyApplication;
import com.team8.cs408.yadonReceiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

import static com.kakao.auth.StringSet.file;

public class AlarmActivity extends AppCompatActivity {
    Intent inputIntent;
    String groupName;
    TimePicker startPointAlarmPicker;
    Spinner periodAlarmSpinner;
    int alarmStart, alarmPeriod;
    private Cursor mCursor;
    public static ArrayList<AlarmManager> AlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().setTitle("그룹알람설정");

        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");

        AlarmList = new ArrayList<AlarmManager>();

        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);   //get columns whose groupName att is set to groupName
        mCursor.moveToNext();
        startPointAlarmPicker = (TimePicker) findViewById(R.id.start_alarm);
        startPointAlarmPicker.setHour(mCursor.getInt(mCursor.getColumnIndex("alarmStart")) / 60);
        startPointAlarmPicker.setMinute(mCursor.getInt(mCursor.getColumnIndex("alarmStart")) % 60);

        periodAlarmSpinner = (Spinner) findViewById(R.id.period_alarm);
        ArrayAdapter periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.period, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        periodAlarmSpinner.setAdapter(periodAdapter);

        alarmPeriod = mCursor.getInt(mCursor.getColumnIndex("alarmPeriod"));
        switch (alarmPeriod) {
            case 6*60*60:
                periodAlarmSpinner.setSelection(0);
                break;
            case 12*60*60:
                periodAlarmSpinner.setSelection(1);
                break;
            case 24*60*60:
                periodAlarmSpinner.setSelection(2);
                break;
            case 48*60*60:
                periodAlarmSpinner.setSelection(3);
                break;
            case 5:
                periodAlarmSpinner.setSelection(4);
                break;
            default:
                periodAlarmSpinner.setSelection(1);
                break;
        }

        mCursor.close();
        periodAlarmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "5초":
                        alarmPeriod = 5;
                        break;
                    case "6시간":
                        alarmPeriod = 6*60*60;
                        break;
                    case "12시간":
                        alarmPeriod = 12*60*60;
                        break;
                    case "1일":
                        alarmPeriod = 24*60*60;
                        break;
                    case "2일":
                        alarmPeriod = 48*60*60;
                        break;
                    default:
                        alarmPeriod = 24*60*60;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alarmPeriod = 24*60*60;
            }
        });

        ImageButton confirmAlarm = (ImageButton) findViewById(R.id.confirm_alarm);
        confirmAlarm.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlarmMessageActivity.class);
                intent.putExtra("groupName", groupName);

                alarmStart = startPointAlarmPicker.getHour() * 60 + startPointAlarmPicker.getMinute();

                MyApplication.mDbOpenHelper.updateTheColumn_alarm(groupName, alarmStart, alarmPeriod);

                AlarmList.clear();
                mCursor = MyApplication.mDbOpenHelper.getAllColumns();
                ArrayList<String> groupNameList = new ArrayList<String>();
                String tempGroupName = "";
                while (mCursor.moveToNext()) {
                    tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
                    if (!groupNameList.contains(tempGroupName)) {
                        groupNameList.add(tempGroupName);
                    }
                }
                mCursor.close();

                AlarmList.clear();
                for (int i = 0; i < groupNameList.size(); i++) {
                    AlarmList.add((AlarmManager)v.getContext().getSystemService(Context.ALARM_SERVICE));
                }
                for (int i = 0; i < groupNameList.size(); i++) {
                    mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupNameList.get(i));
                    mCursor.moveToNext();
                    Intent alarmintent = new Intent(v.getContext(), AlarmReceiver.class);
                    //alarmintent.putExtra("file", file.toString());
                    //intent.putExtra("groupName", groupName);
                    PendingIntent pIntent = PendingIntent.getBroadcast(v.getContext(), 0, alarmintent, PendingIntent.FLAG_UPDATE_CURRENT);

                    //Calendar cal = Calendar.getInstance();
                    //cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 10); // 10초 뒤

                    //long oneday = 24 * 60 * 60 * 1000;// 24시간

                    // 10초 뒤에 시작해서 매일 같은 시간에 반복하기
                    Calendar calendar = Calendar.getInstance();
                    Log.d("alarmPeriod : ", ""+mCursor.getInt(mCursor.getColumnIndex("alarmPeriod")));
                    AlarmList.get(i).setRepeating(AlarmManager.RTC_WAKEUP,
                            //mCursor.getInt(mCursor.getColumnIndex("alarmStart"))*60*100,
                            calendar.getTimeInMillis(),
                            mCursor.getInt(mCursor.getColumnIndex("alarmPeriod"))*10,
                            pIntent);
                    mCursor.close();

                }

                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlarmActivity.this, GroupInfoActivity.class);
        intent.putExtra("groupName", groupName);
        startActivity(intent);
        finish();
    }
}