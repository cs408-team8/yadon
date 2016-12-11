package com.team8.cs408.yadon;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.util.ArrayList;


public class UserSettingViewAdapter extends BaseAdapter {
    private ArrayList<UserSettingViewItem> userSettingViewItem_List;

    public UserSettingViewAdapter() {
        userSettingViewItem_List = new ArrayList<UserSettingViewItem>();
    }

    @Override
    public int getCount() {
        return userSettingViewItem_List.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.usersettingview_item, parent, false);
        }

        TextView optionNameView = (TextView) convertView.findViewById(R.id.item_optionname);
        TextView optionStatusView = (TextView) convertView.findViewById(R.id.item_optionstatus);

        UserSettingViewItem listViewItem = userSettingViewItem_List.get(position);
        Cursor mCursor = MyApplication.mDbOpenHelper.getAllColumnsUserInfo();
        mCursor.moveToNext();
        if(position==0) {
            optionNameView.setText(listViewItem.getOptionName());
            optionStatusView.setText(mCursor.getString(mCursor.getColumnIndex("userName")));
        }
        else if(position==1){
            optionNameView.setText(listViewItem.getOptionName());
            optionStatusView.setText(mCursor.getString(mCursor.getColumnIndex("userBank")));
        }
        else{
            optionNameView.setText(listViewItem.getOptionName());
            optionStatusView.setText(mCursor.getString(mCursor.getColumnIndex("userAccount")));
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return userSettingViewItem_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addItem(String optionName, String optionStatus) {
        UserSettingViewItem item = new UserSettingViewItem();

        item.setOptionName(optionName);
        item.setOptionStatus(optionStatus);

        userSettingViewItem_List.add(item);
    }
    public void changeItem(int position, String newVal){
        userSettingViewItem_List.get(position).setOptionStatus(newVal);
    }

    public void clear(){
        userSettingViewItem_List.clear();
    }

}
