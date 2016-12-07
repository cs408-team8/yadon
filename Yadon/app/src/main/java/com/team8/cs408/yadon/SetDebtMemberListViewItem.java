package com.team8.cs408.yadon;

import android.widget.EditText;

public class SetDebtMemberListViewItem extends Object {
    private String mGroupName;
    private String mMemberName;
    private int mMemberDebt;
    private EditText mMemberEditText;

    public SetDebtMemberListViewItem() {
        mMemberDebt = 0;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public void setMemberName(String memberName) {
        mMemberName = memberName;
    }

    public void setMemberEditText(EditText memberEditText) {
        mMemberEditText = memberEditText;
    }


    public void setMemberDebt(int memberDebt) {
        mMemberDebt = memberDebt;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public int getMemberDebt() {
        return this.mMemberDebt;
    }

    public String getMemberName() {
        return this.mMemberName;
    }

    public EditText getMemberEditText() {
        return this.mMemberEditText;
    }

}