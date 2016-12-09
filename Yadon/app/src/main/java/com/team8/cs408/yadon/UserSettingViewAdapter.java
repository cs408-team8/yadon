package com.team8.cs408.yadon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        optionNameView.setText(listViewItem.getOptionName());
        optionStatusView.setText(listViewItem.getOptionStatus());

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

    public void clear(){
        userSettingViewItem_List.clear();
    }

}
