package com.team8.cs408.yadon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class SetDebtMemberListViewAdapter extends BaseAdapter {
    private ArrayList<SetDebtMemberListViewItem> setDebtGroupMemberListViewItem_List;

    public SetDebtMemberListViewAdapter() {
        setDebtGroupMemberListViewItem_List = new ArrayList<SetDebtMemberListViewItem>();
    }

    @Override
    public int getCount() {
        return setDebtGroupMemberListViewItem_List.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setdebtlistview_item, parent, false);
        }

        TextView memberNameView = (TextView) convertView.findViewById(R.id.item_membername);
        EditText memberDebtView = (EditText) convertView.findViewById(R.id.item_setmemberdebt);

        SetDebtMemberListViewItem listViewItem = setDebtGroupMemberListViewItem_List.get(position);

        memberNameView.setText(listViewItem.getMemberName());
        memberDebtView.setText(Integer.toString(listViewItem.getMemberDebt()));
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return setDebtGroupMemberListViewItem_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addItem(String memberName, int memberDebt) {
        SetDebtMemberListViewItem item = new SetDebtMemberListViewItem();

        item.setMemberName(memberName);
        item.setMemberDebt(memberDebt);
        setDebtGroupMemberListViewItem_List.add(item);
    }

}
