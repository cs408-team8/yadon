package com.team8.cs408.yadon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class GroupListViewAdapter extends BaseAdapter {
    private ArrayList<GroupListViewItem> groupInfoListViewItem_List;

    public GroupListViewAdapter() {
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

        ImageView graphView = (ImageView) convertView.findViewById(R.id.item_piegraph);
        TextView groupNameView = (TextView) convertView.findViewById(R.id.item_groupname);

        GroupListViewItem listViewItem = groupInfoListViewItem_List.get(position);

        graphView.setImageDrawable(listViewItem.getGraph());
        groupNameView.setText(listViewItem.getGroupName());

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

    public void addItem(Drawable graph, String groupName) {
        GroupListViewItem item = new GroupListViewItem();

        item.setGraph(graph);
        item.setGroupName(groupName);

        groupInfoListViewItem_List.add(item);
    }

    public void clear(){
        groupInfoListViewItem_List.clear();
    }
}
