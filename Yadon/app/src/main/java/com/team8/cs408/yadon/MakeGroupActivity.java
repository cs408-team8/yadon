package com.team8.cs408.yadon;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.auth.authorization.AuthorizationResult;

public class MakeGroupActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String[] contactList, new_contactList;
    private String[] nameList, phoneList;
    private ArrayList<String> checkedNames, checkedPhones;
    private int count;
    private ArrayList<String> checkedContacts;
    private EditText groupEditText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makegroup);
        checkedContacts = new ArrayList<String>();
        checkedNames = new ArrayList<String>();
        checkedPhones = new ArrayList<String>();
        count = 0;
        Cursor cursor = getURI();
        int end = cursor.getCount();
        Log.d("number of contacts : ", "" + end);
        contactList = new String[end];
        nameList = new String[end];
        phoneList = new String[end];

        // Store contact information to list.
        if (cursor.moveToFirst()) {
            do {
                if (!cursor.getString(1).startsWith("01")) {    // Cell phone
                    continue;
                }
                contactList[count] = cursor.getString(0);      // Name
                contactList[count] += "\n";
                contactList[count] += cursor.getString(1);     //Phone number
                nameList[count] = cursor.getString(0);
                phoneList[count] = cursor.getString(1);
                count++;
            } while (cursor.moveToNext());
            new_contactList = new String[count];
            for (int i = 0; i < count; i++) {
                new_contactList[i] = contactList[i];       //Clone the info of contacts
            }
        }
        cursor.close();

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, // multi choice layout using the stored contact data.
                new_contactList));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);      //Multi choice option
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkArr = listView.getCheckedItemPositions();
                String checkList = "";
                checkedContacts.clear();
                checkedNames.clear();
                checkedPhones.clear();
                for (int i = 0; i < listView.getCount(); i++) {
                    if (checkArr.get(i)) {
                        checkedContacts.add(contactList[i]);
                        checkedNames.add(nameList[i]);
                        checkedPhones.add(phoneList[i]);
                        if (checkList == "") {                  // for first element
                            checkList += contactList[i];
                        } else {
                            checkList += "\n";
                            checkList += contactList[i];
                        }
                    }
                }
                Toast.makeText(MakeGroupActivity.this, checkList, Toast.LENGTH_SHORT).show();
            }
        });

        // After selecting group members, we transport the data to Home activity
        ImageButton confirm = (ImageButton) findViewById(R.id.confirm);
        groupEditText = (EditText) findViewById(R.id.groupname);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = "";
                // if there is no input text, the group name sets to current time.
                if ((groupName = groupEditText.getText().toString()).length() <= 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm", Locale.KOREA);
                    groupName = df.format(new Date());
                }

                Intent intent = new Intent();
                intent.putStringArrayListExtra("checkedNames", checkedNames);
                intent.putStringArrayListExtra("checkedPhones", checkedPhones);
                intent.putExtra("groupName", groupName);
                setResult(RESULT_OK, intent);
                finish();                   // go to onActivityResult in HomeActivity.java
            }
        });
    }

    // get contacts information.
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
