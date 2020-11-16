package com.example.payeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> orderList;
    private HashMap<String, List<String>> all_orders;

    public ExpandableListViewAdapter(Context context, List<String> orderList, HashMap<String, List<String>> all_orders) {
        this.context = context;
        this.orderList = orderList;
        this.all_orders = all_orders;
    }

    @Override
    public int getGroupCount() {
        return this.orderList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.all_orders.get(this.orderList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.orderList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.all_orders.get(this.orderList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String chapterTitle = (String) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView orderInfo = convertView.findViewById(R.id.title_order);
        orderInfo.setText(chapterTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String topicTitle = (String) getChild(groupPosition, childPosition);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_topics_list, null);
        }

        TextView orderTopic = convertView.findViewById(R.id.textView_order_info);
        orderTopic.setText(topicTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
