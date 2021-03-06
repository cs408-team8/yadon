package com.team8.cs408.yadon;

import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.team8.cs408.yadonDataBase.MyApplication;

import java.util.ArrayList;


public class SetDebtMemberListViewAdapter extends BaseAdapter {
    private ArrayList<SetDebtMemberListViewItem> setDebtGroupMemberListViewItem_List;
    SetDebtActivity activity;
    public SetDebtMemberListViewAdapter(SetDebtActivity inactivity) {
        setDebtGroupMemberListViewItem_List = new ArrayList<SetDebtMemberListViewItem>();
        activity = inactivity;
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
        final EditText memberDebtView = (EditText) convertView.findViewById(R.id.item_setmemberdebt);
        final SetDebtMemberListViewItem listViewItem = setDebtGroupMemberListViewItem_List.get(position);
        memberNameView.setText(listViewItem.getMemberName());
        memberDebtView.setText(Integer.toString(listViewItem.getMemberDebt()));
        memberDebtView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus  == false){
                    Log.d("onTextChanged !! ", "" + pos);
                    String s;
                    if (memberDebtView.getText().toString().length() <= 0) {
                        s = "0";
                    }
                    else{
                        s = memberDebtView.getText().toString();
                    }
                    MyApplication.mDbOpenHelper.updateTheColumn_debt(listViewItem.getGroupName(),
                            listViewItem.getMemberName(), Integer.parseInt(s));
                    listViewItem.setMemberDebt(Integer.parseInt(s));
                    Log.d("in onTextChnaged! ", "name : " + listViewItem.getMemberName() + "debt : " + Integer.toString(listViewItem.getMemberDebt()));
                }
                int sum=0;
                for(int i=0;i<activity.adapter.getCount();i++){
                    SetDebtMemberListViewItem item = (SetDebtMemberListViewItem) activity.adapter.getItem(i);
                    sum+=item.getMemberDebt();
                }
                activity.total.setText(String.valueOf(sum));
            }
        });

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


    public void addItem(String groupName, String memberName, int memberDebt) {
        SetDebtMemberListViewItem item = new SetDebtMemberListViewItem();

        item.setGroupName(groupName);
        item.setMemberName(memberName);
        item.setMemberDebt(memberDebt);

        setDebtGroupMemberListViewItem_List.add(item);
    }

    public void clear() {
        setDebtGroupMemberListViewItem_List.clear();
    }

    private class MyWatcher implements TextWatcher {
        private EditText editText;
        private ClipData.Item item;

        public MyWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.item = (ClipData.Item) editText.getTag();
            if (item != null) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}