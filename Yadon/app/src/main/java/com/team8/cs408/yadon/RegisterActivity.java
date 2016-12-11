package com.team8.cs408.yadon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.team8.cs408.yadonDataBase.DbOpenHelper;
import com.team8.cs408.yadonDataBase.MyApplication;


public class RegisterActivity extends AppCompatActivity {
    String bank="";
    String[] banks = {"우체국","우리은행","농협","신한은행","KEB하나은행"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("개인정보등록");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,banks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank=banks[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bank="";
            }
        });

        final EditText regname = (EditText) findViewById(R.id.regnameeditText);
        final EditText regaccount = (EditText) findViewById(R.id.regaccounteditText);
        Button button = (Button) findViewById(R.id.registbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regname.getText().toString().length()<=0 || bank.length()<=0 || regaccount.getText().toString().length()<=0){
                    Toast.makeText(getApplicationContext(), "이름, 주거래은행, 계좌번호를 모두 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyApplication.mDbOpenHelper.insertColumnUserBasicInfo(
                            regname.getText().toString(), bank, regaccount.getText().toString(), Boolean.TRUE, "(그룹이름) 모임을 계산한 " + regname.getText().toString() + "입니다. (사람이름)님 " + bank + " " + regaccount.getText().toString() + "로 (청구금액)원 입금 부탁드립니다. (이 메세지는 야 돈!에서 발송된 메시지입니다. 입금해주실 때까지 주기적으로 발송됩니다.)"

                    );
                    Intent intent= new Intent(RegisterActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }
}
