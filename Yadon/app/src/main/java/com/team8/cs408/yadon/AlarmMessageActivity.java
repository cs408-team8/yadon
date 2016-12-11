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
    private BroadcastReceiver sentBroadcastReceiver;
    private BroadcastReceiver deliveredBroadcastReceiver;

    public void sendSMS(String memberPhone, String memberName, int memberDebt) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        sentBroadcastReceiver = new BroadcastReceiver() {
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
        };

        deliveredBroadcastReceiver = new BroadcastReceiver() {
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
        };

        registerReceiver(sentBroadcastReceiver, new IntentFilter("SMS_SENT_ACTION"));
        registerReceiver(deliveredBroadcastReceiver, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        Cursor cursor = MyApplication.mDbOpenHelper.getAllColumnsUserInfo();
        cursor.moveToNext();
        String msg = groupName+" 모임을 계산한 " + cursor.getString(cursor.getColumnIndex("userName")) + "입니다. "+memberName+"님 " + cursor.getString(cursor.getColumnIndex("userBank")) + " " + cursor.getString(cursor.getColumnIndex("userAccount")) + "로 "+memberDebt+"원 입금 부탁드립니다. (이 메세지는 야 돈!에서 발송된 메시지입니다. 입금해주실 때까지 주기적으로 발송됩니다.)";
        mSmsManager.sendTextMessage(memberPhone, null, msg,
                sentIntent, deliveredIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sentBroadcastReceiver);
        unregisterReceiver(deliveredBroadcastReceiver);
    }
}