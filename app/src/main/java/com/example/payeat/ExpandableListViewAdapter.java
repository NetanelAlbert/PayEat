package com.example.payeat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
//    public EditText static cost; not need this after connect to the database

    private List<String> _listDataHeader;
    private HashMap<String, Order> _listChildData;
    private UpdateCostFragment costFragment;
    private DeleteDishFragment deleteDishFragment;
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
                costFragment = UpdateCostFragment.newInstance(groupPosition, childPosition);
                costFragment.show(FmBase, "UpdateCostFragment");
            }
        });
        Button deleteDishButton = convertView.findViewById(R.id.button_delete_dish);
        deleteDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDishFragment = DeleteDishFragment.newInstance(groupPosition, childPosition);
                deleteDishFragment.show(FmBase, "DeleteFragment");
            }
        });

//        if(isLastChild) {
//            LinearLayout buttonContainer = (LinearLayout) convertView.findViewById(R.id.layout_order_topics);
//            Button myButton = new Button(_context);
//            myButton.setText("Press Me");
//
//            buttonContainer.addView(myButton);
//
//        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        Toast.makeText(,"you press: " + childPosition + " in: " + groupPosition, Toast.LENGTH_SHORT).show();
        return true;
    }
}
