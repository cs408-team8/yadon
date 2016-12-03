package com.team8.cs408.yadon;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class GroupMemberListViewItem extends Object{

    private Drawable mProfile;
    private String mMemberName;
    private String mMemberPhone;
    private int mMemberDebt;
    private boolean mRepaid;

    public GroupMemberListViewItem() {
        mRepaid = false;
    }

    public void setProfile(Drawable profile) {
        mProfile = profile;
    }

    public void setMemberName(String memberName) {
        mMemberName = memberName;
    }

    public void setMemberPhone(String memberPhone) {
        mMemberPhone = memberPhone;
    }

    public void setMemberDebt(int memberDebt) {
        mMemberDebt = memberDebt;
    }

    public void setRepaid() {
        mRepaid = true;
    }

    public Drawable getProfile() {
        return this.mProfile;
    }

    public int getMemberDebt() {
        return this.mMemberDebt;
    }

    public String getMemberName() {
        return this.mMemberName;
    }

    public String getMemberPhone() {
        return this.mMemberPhone;
    }

    public boolean isRepaid() {
        return this.mRepaid;
    }
}