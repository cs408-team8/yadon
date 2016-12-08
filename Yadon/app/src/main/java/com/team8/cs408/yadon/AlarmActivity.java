package com.team8.cs408.yadon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AlarmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        TimePicker startPointAlarmPicker = (TimePicker) findViewById(R.id.start_alarm);
        startPointAlarmPicker.setHour(12);
        startPointAlarmPicker.setMinute(0);

        Spinner periodAlarmSpinner = (Spinner) findViewById(R.id.period_alarm);
        ArrayAdapter periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.period, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        periodAlarmSpinner.setAdapter(periodAdapter);

    }
}
