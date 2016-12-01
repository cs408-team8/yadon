package com.team8.cs408.yadon;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class GroupInfoListViewItem {
    private Drawable mPieGraph;
    private String mGroupName;
    private ArrayList<String> mMemberNames;
    private ArrayList<String> mMemberPhones;

    public GroupInfoListViewItem(){
        mMemberNames = new ArrayList<String>();
        mMemberPhones = new ArrayList<String>();
    }
    public void setGraph(Drawable pieGraph) {
        mPieGraph = pieGraph;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public void setMemberNames(ArrayList<String> memberNames){
        mMemberNames = memberNames;
    }

    public void setMemberPhones(ArrayList<String> memberPhones){
        mMemberPhones = memberPhones;
    }

    public Drawable getGraph() {
        return this.mPieGraph;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public ArrayList<String> getMemberNames() {
        return this.mMemberNames;
    }

    public ArrayList<String> getMemberPhones() {
        return this.mMemberPhones;
    }
}