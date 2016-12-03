package com.team8.cs408.yadon;

public class SetDebtMemberListViewItem {

    private String mMemberName;
    private int mMemberDebt;

    public SetDebtMemberListViewItem() {
        mMemberDebt = 0;
    }

    public void setMemberName(String memberName) {
        mMemberName = memberName;
    }


    public void setMemberDebt(int memberDebt) {
        mMemberDebt = memberDebt;
    }

    public int getMemberDebt() {
        return this.mMemberDebt;
    }

    public String getMemberName() {
        return this.mMemberName;
    }

}