package com.team8.cs408.yadonReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("!!!!!!!!!1", "!!!!!"+intent.getStringExtra("groupName"));
    }
}
