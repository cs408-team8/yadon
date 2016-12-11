package com.team8.cs408.yadonReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import android.provider.Telephony;
import android.util.Log;

import com.team8.cs408.yadon.LoadingActivity;
import com.team8.cs408.yadonDataBase.MyApplication;

import static android.R.attr.action;

public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private Cursor mCursor;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                String format = bundle.getString("format");
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i], format);
            }
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/", Locale.KOREA);

            String receiveYear = mDateFormat.format(curDate);
            String receiveNumber = smsMessage[0].getOriginatingAddress();
            String Message = smsMessage[0].getMessageBody();

            ArrayList<String> lineList = new ArrayList<String>();

            try {
                StringTokenizer st = new StringTokenizer(Message, "\n");
                while (st.hasMoreTokens()) {
                    lineList.add(st.nextToken().trim());
                }
                if(lineList.get(0).equals("[Web발신]")){
                    String date = lineList.get(1).split(" ")[0].substring(2);
                    receiveYear = receiveYear.concat(date);
                    String time = lineList.get(1).split(" ")[1];
                    String myAccount = lineList.get(2).split(" ")[0];
                    String senderName = lineList.get(2).split(" ")[1];
                    String moneyString = lineList.get(3).split(" ")[1];
                    moneyString = moneyString.substring(0, moneyString.length() - 1);
                    String[] moneyArrayBeforeConcat = moneyString.split(",");
                    moneyString = ""; // reuse..
                    for (int i = 0; i < moneyArrayBeforeConcat.length; i++) {
                        moneyString += moneyArrayBeforeConcat[i];
                    }
                    int receivedMoney = Integer.parseInt(moneyString);
                    Log.d("therefore,, ", receiveYear + " " + time + " " + myAccount + " " + senderName + " " + receivedMoney);

                    //update to Database
                    mCursor = MyApplication.mDbOpenHelper.getAllColumns();
                    String tempGroupName = "";
                    int tempDebt = 0;
                    while (mCursor.moveToNext()) {
                        if (receivedMoney == 0) {
                            break;
                        }
                        if (senderName.equals(mCursor.getString(mCursor.getColumnIndex("name")))) {
                            tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
                            tempDebt = mCursor.getInt(mCursor.getColumnIndex("debt"));
                            if (receivedMoney > tempDebt) {
                                MyApplication.mDbOpenHelper.updateTheColumn_debt(tempGroupName, senderName, 0);
                                receivedMoney -= tempDebt;
                            } else {
                                MyApplication.mDbOpenHelper.updateTheColumn_debt(tempGroupName, senderName, tempDebt - receivedMoney);
                                receivedMoney = 0;
                            }
                        } else {
                        }
                    }
                    mCursor.close();
                    updateDBGroupState();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("SMSReceiver : ", "Wrong Format");
            }

            Intent newintent = new Intent(context, LoadingActivity.class);
            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newintent);
        }
    }

    public void updateDBGroupState() {
        mCursor = MyApplication.mDbOpenHelper.getAllColumns();
        String tempGroupName = "";
        ArrayList<String> groupNameList = new ArrayList<String>();
        int tempDebt = 0;
        while (mCursor.moveToNext()) {
            tempGroupName = mCursor.getString(mCursor.getColumnIndex("groupName"));
            if (!groupNameList.contains(tempGroupName)) {
                groupNameList.add(tempGroupName);
            }
        }
        mCursor.close();
        for (int i = 0; i < groupNameList.size(); i++) {
            int groupTotalDebt = 0;
            int debtSetup = 0;
            mCursor = MyApplication.mDbOpenHelper.getGroupColumns(groupNameList.get(i));
            while (mCursor.moveToNext()) {
                groupTotalDebt += mCursor.getInt(mCursor.getColumnIndex("debt"));
                debtSetup = mCursor.getInt(mCursor.getColumnIndex("debtSetup"));
            }
            if (debtSetup != 0 && groupTotalDebt == 0) {
                MyApplication.mDbOpenHelper.updateColumns_groupState_collectionCompleted(groupNameList.get(i), 1);  // completed
            }
            mCursor.close();
        }
    }
}
