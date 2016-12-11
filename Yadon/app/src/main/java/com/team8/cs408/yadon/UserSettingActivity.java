package com.team8.cs408.yadon;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.team8.cs408.yadonDataBase.DbOpenHelper;
import com.team8.cs408.yadonDataBase.MyApplication;
import com.team8.cs408.yadonDataBase.UserBasicInfoDB;

/**
 * Created by newwhite on 2016. 12. 9..
 */

public class UserSettingActivity extends AppCompatActivity {
    ListView userset;
    UserSettingActivity activity=this;
    Cursor mCursor;
    UserSettingViewAdapter adapter;
    String[] banks = {"우체국","우리은행","농협","신한은행","KEB하나은행"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        getSupportActionBar().setTitle("유저정보설정");

        userset = (ListView)findViewById(R.id.usersetting_list);
        adapter = new UserSettingViewAdapter();
        mCursor = MyApplication.mDbOpenHelper.getAllColumnsUserInfo();
        mCursor.moveToNext();
        adapter.addItem("이름",mCursor.getString(mCursor.getColumnIndex("userName")));
        adapter.addItem("은행",mCursor.getString(mCursor.getColumnIndex("userBank")));
        adapter.addItem("계좌번호",mCursor.getString(mCursor.getColumnIndex("userAccount")));
        userset.setAdapter(adapter);
        userset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final UserSettingViewItem selItem = (UserSettingViewItem) adapter.getItem(position);
                if (position==0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                    alert.setTitle("이름변경");
                    alert.setMessage("새로운 이름을 입력하세요.");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(activity);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            // Do something with value!
                            if(value.toString().length()>0) {
                                MyApplication.mDbOpenHelper.updateUserInfo_Name(value.toString());
                                MyApplication.mDbOpenHelper.updateUserInfo_SampleMessage("(그룹이름) 모임을 계산한 " + mCursor.getString(mCursor.getColumnIndex("userName")) + "입니다. (사람이름)님 " + mCursor.getString(mCursor.getColumnIndex("userBank")) + " " + mCursor.getString(mCursor.getColumnIndex("userAccount")) + "로 (청구금액)원 입금 부탁드립니다. (이 메세지는 야 돈!에서 발송된 메시지입니다. 입금해주실 때까지 주기적으로 발송됩니다.)");
                                adapter.changeItem(0, value.toString());
                                updateUserSettingListView();
                            }
                        }
                    });


                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Canceled.
                                }
                            });

                    alert.show();
                }
                else if(position==1){
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("주거래은행");
                    alert.setSingleChoiceItems(banks,-1,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int item){
                            MyApplication.mDbOpenHelper.updateUserInfo_Bank(banks[item].toString());
                            MyApplication.mDbOpenHelper.updateUserInfo_SampleMessage("(그룹이름) 모임을 계산한 " + mCursor.getString(mCursor.getColumnIndex("userName")) + "입니다. (사람이름)님 " + mCursor.getString(mCursor.getColumnIndex("userBank")) + " " + mCursor.getString(mCursor.getColumnIndex("userAccount")) + "로 (청구금액)원 입금 부탁드립니다. (이 메세지는 야 돈!에서 발송된 메시지입니다. 입금해주실 때까지 주기적으로 발송됩니다.)");
                            adapter.changeItem(1,banks[item].toString());
                            updateUserSettingListView();
                            dialog.cancel();
                        }
                    });
                    alert.show();

                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                    alert.setTitle("계좌번호변경");
                    alert.setMessage("새로운 계좌번호를 입력하세요.");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(activity);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            if(value.toString().length()>0) {
                                MyApplication.mDbOpenHelper.updateUserInfo_Account(value.toString());
                                MyApplication.mDbOpenHelper.updateUserInfo_SampleMessage("(그룹이름) 모임을 계산한 " + mCursor.getString(mCursor.getColumnIndex("userName")) + "입니다. (사람이름)님 " + mCursor.getString(mCursor.getColumnIndex("userBank")) + " " + mCursor.getString(mCursor.getColumnIndex("userAccount")) + "로 (청구금액)원 입금 부탁드립니다. (이 메세지는 야 돈!에서 발송된 메시지입니다. 입금해주실 때까지 주기적으로 발송됩니다.)");
                                adapter.changeItem(2, value.toString());
                                updateUserSettingListView();
                            }
                        }
                    });


                    alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                        }
                    });

                    alert.show();
                }
            }
        });


    }

    private void updateUserSettingListView(){
        adapter.clear();
        adapter.addItem("이름",mCursor.getString(mCursor.getColumnIndex("userName")));
        adapter.addItem("은행",mCursor.getString(mCursor.getColumnIndex("userBank")));
        adapter.addItem("계좌번호",mCursor.getString(mCursor.getColumnIndex("userAccount")));
        adapter.notifyDataSetChanged();


    }
    public void onBackPressed() {
        Intent intent = new Intent(UserSettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}



// 상세 메세지 설정
// 유저정보 변경 ->
// 알람성정 돈 다 받을때 push 알림 받을까 안받을까
// 버전정보