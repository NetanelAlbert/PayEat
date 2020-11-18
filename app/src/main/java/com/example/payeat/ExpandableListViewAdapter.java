package com.example.payeat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, Order> _listChildData;
    private UpdateCostFragment costFragment;
    FragmentManager FmBase;

    public ExpandableListViewAdapter(Context context, List<String> _listDataHeader,
                                     HashMap<String, Order> _listChildData, FragmentManager FmMain) {
        this._context = context;
        this._listDataHeader = _listDataHeader;
        this._listChildData = _listChildData;
        this.FmBase = FmMain;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listChildData.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listChildData.get(this._listDataHeader.get(groupPosition)).get(childPosition);
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView orderInfo = convertView.findViewById(R.id.title_order);
        orderInfo.setText(chapterTitle);

        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Dish topicTitle = (Dish) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_topics_list, null);
        }

        EditText dish_name = convertView.findViewById(R.id.editText_dish_name);
        dish_name.setText(topicTitle.getName());

        EditText cost = convertView.findViewById(R.id.editText_cost);
        cost.setText("" + topicTitle.getPrice());

        EditText description = convertView.findViewById(R.id.editText_description);
        description.setText(topicTitle.getDesc());

        Button editCostButton = convertView.findViewById(R.id.button_edit_cost);
        editCostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (v.getId()) {
                    case R.id.button_edit_cost:
                        costFragment = UpdateCostFragment.newInstance(this);
                        costFragment.show(FmBase, "UpdateCostFragment");
                        break;
//                    case R.id.button_update_cost:
//                        String new_costS = costFragment.getCost();
//                        if(new_costS == null || new_costS.length() == 0)
//                            return;
//                        int new_cost = Integer.parseInt(new_costS);
//                        costFragment.dismiss();
//
//                        break;
//                    case R.id.button_cancel_cost:
//                        costFragment.dismiss();
//                        break;
                    default:
                }
            }
        });
        Button deleteDishButton = convertView.findViewById(R.id.button_delete_dish);
        deleteDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(Drawable.createFromPath("#000"));
                Order order = (Order)  _listChildData.get((String) getGroup(groupPosition));
                Dish deletedDish = order.deleteDish(childPosition);
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        Toast.makeText(,"you press: " + childPosition + " in: " + groupPosition, Toast.LENGTH_SHORT).show();
        return true;
    }

}
