package com.team8.cs408.yadonReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.provider.Telephony;
import android.util.Log;

import com.team8.cs408.yadon.LoadingActivity;

import static android.R.attr.action;

public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i=0;i<messages.length;i++){
                String format = bundle.getString("format");
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i], format);
            }
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);

            String receiveDate = mDateFormat.format(curDate);
            String receiveNumber = smsMessage[0].getOriginatingAddress();
            String receiveMessage = smsMessage[0].getMessageBody();
            Log.d("receivedDate : ", receiveDate);
            Log.d("receiveNumber : ", receiveNumber);
            Log.d("receiveMessage : ", receiveMessage);

            Intent newintent = new Intent(context, LoadingActivity.class);
            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newintent);
        }
    }
}
