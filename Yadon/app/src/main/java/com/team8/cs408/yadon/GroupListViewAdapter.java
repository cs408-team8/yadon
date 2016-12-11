package com.team8.cs408.yadon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team8.cs408.yadonDraw.GraphView;

import java.util.ArrayList;


public class GroupListViewAdapter extends BaseAdapter {
    private ArrayList<GroupListViewItem> groupInfoListViewItem_List;
    Context context;
    String groupName;

    public GroupListViewAdapter(Context context) {
        this.context = context;
        groupInfoListViewItem_List = new ArrayList<GroupListViewItem>();
    }

    @Override
    public int getCount() {
        return groupInfoListViewItem_List.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grouplistview_item, parent, false);
        }


        TextView groupNameView = (TextView) convertView.findViewById(R.id.item_groupname);
        LinearLayout groupListViewItemGraphLayout = (LinearLayout) convertView.findViewById(R.id.graph_grouplist);
        ImageButton groupSetAlarmButton = (ImageButton) convertView.findViewById(R.id.group_setalarm);
        groupSetAlarmButton.setFocusable(false);

        GraphView pieGraphView = new GraphView(context); //graph

        GroupListViewItem listViewItem = groupInfoListViewItem_List.get(position);
        if(listViewItem.getTotal()==listViewItem.getRepaid()){
            convertView.setBackgroundColor(Color.argb(80,0,0,0));
        }
        else{
            convertView.setBackgroundColor(Color.argb(50,0,0,255));
        }
        groupName =listViewItem.getGroupName();
        groupNameView.setText(groupName);
        pieGraphView.setArgs(listViewItem.getTotal(), listViewItem.getRepaid());
        pieGraphView.setPieGraph();
        groupListViewItemGraphLayout.addView(pieGraphView);
        groupSetAlarmButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, AlarmActivity.class);
                intent.putExtra("groupName", groupName);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return groupInfoListViewItem_List.get(position);
    }

    public void addItem(String groupName, int total, int repaid) {
        GroupListViewItem item = new GroupListViewItem();

        item.setGroupName(groupName);
        item.setArgs(total,repaid);

        groupInfoListViewItem_List.add(item);
    }

    public void clear(){
        groupInfoListViewItem_List.clear();
    }
}
