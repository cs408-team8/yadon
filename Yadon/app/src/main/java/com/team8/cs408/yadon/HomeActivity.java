package com.team8.cs408.yadon;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageButton addGroupButton = (ImageButton) findViewById(R.id.addGroup);
        addGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MakeGroupActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> checkedContacts = data.getStringArrayListExtra("checkedContacts");
            for (int i = 0; i < checkedContacts.size(); i++) {
                Log.d("checkedContacts : ", checkedContacts.get(i));
            }
            TextView text1 = (TextView) findViewById(R.id.HometextView1);
            TextView text2 = (TextView) findViewById(R.id.HometextView2);
            String str1 = "체크된 그룹 맴버 수 : " + Integer.toString(checkedContacts.size());
            String str2 = "체크된 그룹 정보 :\n";
            for (int i = 0; i < checkedContacts.size(); i++) {
                str2 += checkedContacts.get(i) + "\n";
            }
            text1.setText(str1);
            text2.setText(str2);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

}
