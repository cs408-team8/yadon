package com.team8.cs408.yadon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

public class HomeActivity extends AppCompatActivity {
    private KakaoLink mKakaoLink;
    private KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitKakaoLink();
        SendKakaoMessage();
    }

    private void InitKakaoLink() {
        try {
            mKakaoLink = KakaoLink.getKakaoLink(this);
            mKakaoTalkLinkMessageBuilder = mKakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    private void SendKakaoMessage() {
        try {
            mKakaoTalkLinkMessageBuilder.addText("TEST 메시지");
            mKakaoLink.sendMessage(mKakaoTalkLinkMessageBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
}
