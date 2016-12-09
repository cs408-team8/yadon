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


public class GroupMemberListViewAdapter extends BaseAdapter {
    private ArrayList<GroupMemberListViewItem> groupMemberListViewItem_List;

    public GroupMemberListViewAdapter() {
        groupMemberListViewItem_List = new ArrayList<GroupMemberListViewItem>();
    }

    @Override
    public int getCount() {
        return groupMemberListViewItem_List.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.groupmemberlistview_item, parent, false);
        }

        ImageView profileView = (ImageView) convertView.findViewById(R.id.item_confirmimage);
        TextView memberNameView = (TextView) convertView.findViewById(R.id.item_membername);
        TextView memberDebtView = (TextView) convertView.findViewById(R.id.item_memberdebt);

        GroupMemberListViewItem listViewItem = groupMemberListViewItem_List.get(position);

        profileView.setImageDrawable(listViewItem.getProfile());
        memberNameView.setText(listViewItem.getMemberName());
        memberDebtView.setText(Integer.toString(listViewItem.getMemberDebt()));
        if(listViewItem.getMemberDebt()==0/*change condition*/){
            convertView.setBackgroundColor(Color.argb(50,0,0,255));
        }
        else{
            convertView.setBackgroundColor(Color.argb(50,255,0,0));
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return groupMemberListViewItem_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addItem(Drawable profile, String memberName, String memberPhone, int memberDebt) {
        GroupMemberListViewItem item = new GroupMemberListViewItem();

        item.setProfile(profile);
        item.setMemberName(memberName);
        item.setMemberPhone(memberPhone);
        item.setMemberDebt(memberDebt);

        groupMemberListViewItem_List.add(item);
    }

    public void clear(){
        groupMemberListViewItem_List.clear();
    }

}
