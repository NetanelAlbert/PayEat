package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.DataChangeListener;
import com.example.payeat.Database;
import com.example.payeat.fragments.DeleteDishFragment;
import com.example.payeat.Dish;
import com.example.payeat.Order;
import com.example.payeat.R;
import com.example.payeat.fragments.UpdateCostFragment;
import com.example.payeat.fragments.ChooseTableFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistOrdersActivity extends AppCompatActivity implements DataChangeListener {

    private ExpandableListViewAdapter listViewAdapter;
    private ExpandableListView expandableListView;
    private SearchView searchView;
    private ChooseTableFragment chooseTableFragment;

    private List<String> idOrderList;
    private HashMap<String, Order> all_orders; // order_id -> Order/Dishes

    Firebase firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exist_orders);

//        findViewById(R.id.button_delete_dish).setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.orders);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu:
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        intent.putExtra("mode manager", true);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.orders:
                        return true;
                    case R.id.restaurant_capacity:
                        startActivity(new Intent(getApplicationContext(), RestaurantOccupancyActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        expandableListView = findViewById(R.id.listView_orders);
        idOrderList = new ArrayList<String>(); // id Order (the title of the Order whatever)
        all_orders = new HashMap<String, Order>(); // each raw in the Order. what is the Order info.
        notifyOnChange();
        listViewAdapter = new ExpandableListViewAdapter(this, idOrderList, all_orders, getSupportFragmentManager());
        expandableListView.setAdapter(listViewAdapter);

        searchView = findViewById(R.id.SearchView_orders);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> filteredOrders = new ArrayList<>();
                HashMap<String, Order> all_ordersFiltered = new HashMap<>();

                for(String order_id: all_orders.keySet())
                {
                    if(order_id.toLowerCase().contains(newText.toLowerCase()))
                    {
                        filteredOrders.add(order_id);
                        all_ordersFiltered.put(order_id, all_orders.get(order_id));
                    }
                }
                listViewAdapter = new ExpandableListViewAdapter(getApplicationContext(),
                        filteredOrders, all_ordersFiltered, getSupportFragmentManager());
                expandableListView.setAdapter(listViewAdapter);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }

    @Override
    public void notifyOnChange() {
        final ArrayList<Order> orders = Database.getOrders();
        int i = 0;
        for (Order order: orders) {
            int table_number = order.getTable_number();
            idOrderList.add("שולחן " + table_number);
            all_orders.put(idOrderList.get(i), order);
            i++;
        }
    }

    class ExpandableListViewAdapter extends BaseExpandableListAdapter {

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
                convertView = inflater.inflate(R.layout.order_title, null);
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
}