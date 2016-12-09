package com.team8.cs408.yadon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.util.ArrayList;

public class AlarmMessageActivity extends AppCompatActivity {
    private Intent inputIntent;
    String groupName;
    Cursor mCursor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputIntent = getIntent();
        groupName = inputIntent.getStringExtra("groupName");
        Log.d("in alarmMessage ", groupName);
        ArrayList<String> memberNames = new ArrayList<String>();
        ArrayList<String> memberPhones = new ArrayList<String>();
        ArrayList<Integer> memberDebts = new ArrayList<Integer>();
        mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupName);
        while (mCursor.moveToNext()) {
            memberNames.add(mCursor.getString(mCursor.getColumnIndex("name")));
            memberPhones.add(mCursor.getString(mCursor.getColumnIndex("phoneNumber")));
            memberDebts.add(mCursor.getInt(mCursor.getColumnIndex("debt")));
        }
        mCursor.close();
        for (int i = 0; i < memberDebts.size(); i++) {
            if (memberDebts.get(i) > 0) {
                sendSMS(memberPhones.get(i), memberNames.get(i), memberDebts.get(i));
            }
        }
        Intent intent = new Intent(AlarmMessageActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendSMS(String memberPhone, String memberName, int memberDebt) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(memberPhone, null, memberName + "님, 아직 " + memberDebt + "원 갚지 않으셨습니다.",
                sentIntent, deliveredIntent);
    }
}
