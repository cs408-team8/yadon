package com.team8.cs408.yadon;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.provider.ContactsContract;

import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.auth.authorization.AuthorizationResult;

public class MakeGroupActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String[] name, new_name;
    private int count;
    private ArrayList<String> checkedContacts;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);
        checkedContacts = new ArrayList<String>();
        count = 0;
        Cursor cursor = getURI();
        int end = cursor.getCount();
        Log.d("number of contacts : ", "" + end);
        name = new String[end];

        // Getting contact information
        if (cursor.moveToFirst()) {
            do {
                if (!cursor.getString(1).startsWith("01")) {    // Cell phone
                    continue;
                }
                name[count] = cursor.getString(0);      // Name
                name[count] += "\n";
                name[count] += cursor.getString(1);     //Phone number
                count++;
            } while (cursor.moveToNext());
            new_name = new String[count];
            for (int i = 0; i < count; i++) {
                new_name[i] = name[i];       //Clone the info of contacts
            }
        }
        cursor.close();
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, // multi choice layout
                new_name));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);      //Multi choice option
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkArr = listView.getCheckedItemPositions();
                String checkList = "";
                checkedContacts.clear();
                for (int i = 0; i < listView.getCount(); i++) {
                    if (checkArr.get(i)) {
                        checkedContacts.add(name[i]);
                        if (checkList == "") {
                            checkList += name[i];
                        } else {
                            checkList += "\n";
                            checkList += name[i];
                        }
                    }
                }
                Toast.makeText(MakeGroupActivity.this, checkList, Toast.LENGTH_SHORT).show();
            }
        });

        // After selecting group members, we transport the data to Home activity
        ImageButton confirm = (ImageButton) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /*
                intent.putExtra("num_checked", checkedContacts.size());
                for(int i=0;i<checkedContacts.size();i++){
                    intent.putExtra("index"+ i, checkedContacts.get(i));
                }
                */
                intent.putStringArrayListExtra("checkedContacts", checkedContacts);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private Cursor getURI() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts._ID
        };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
        return getContentResolver().query(uri, projection, null, null, sortOrder);

    }
}
