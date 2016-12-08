package com.team8.cs408.yadon;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class GroupListViewItem {
    private Drawable mPieGraph;
    private String mGroupName;
    private int mTotal;
    private int mRepaid;


    public GroupListViewItem() {
    }

    public void setGraph(Drawable pieGraph) {
        mPieGraph = pieGraph;
    }

    public void setArgs(int total, int repaid){
        mTotal = total;
        mRepaid = repaid;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public Drawable getGraph() {
        return this.mPieGraph;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public int getTotal(){
        return this.mTotal;
    }

    public int getRepaid(){
        return this.mRepaid;
    }
}