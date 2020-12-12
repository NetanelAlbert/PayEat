
//
// Author: Ido Shapira
//
// This activity is shown right after the manager decided he wants to view the exist orders on the app.
// The manager cam picks one of two options:
// 1. View the current live orders
// 2. Can edit a specific order:
//      *Delete dish from an order.
//      *Update dish price (discount).
// Order can be found using the search bar, meaning the manager can get an order by table number.

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

import com.example.payeat.interfaces.DataChangeListener;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.fragments.DeleteDishFragment;
import com.example.payeat.dataObjects.Dish;
import com.example.payeat.dataObjects.Order;
import com.example.payeat.R;
import com.example.payeat.fragments.UpdateCostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExistOrdersActivity extends AppCompatActivity implements DataChangeListener {

    private ExpandableListViewAdapter listViewAdapter;
    private ExpandableListView expandableListView;
    private SearchView searchView;

    private List<String> table_orders_list;
    private HashMap<String, Order> all_orders; // table_number -> Order/Dishes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exist_orders);

        // Setup bottom navigation view --> (menu, orders, capacity)
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
                        startActivity(new Intent(getApplicationContext(), RestaurantCapacityActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        expandableListView = findViewById(R.id.listView_orders);
        table_orders_list = new ArrayList<String>();
        all_orders = new HashMap<String, Order>();
        listViewAdapter = new ExpandableListViewAdapter(this, table_orders_list, all_orders, getSupportFragmentManager());
        expandableListView.setAdapter(listViewAdapter);

        // Get exist order from database and build table_orders_list all_orders
        notifyOnChange();

        // Setup search view --> search by table number
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
        final ArrayList<Order> orders = Database.getOrders("live_orders");
        table_orders_list.clear();
        all_orders.clear();
        int i = 0;
        for (Order order: orders) {
            int table_number = order.getTable_number();
            table_orders_list.add("שולחן " + table_number);
            all_orders.put(table_orders_list.get(i), order);
            i++;
        }
        listViewAdapter.notifyDataSetChanged();
    }

    class ExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> tableOrdersList;
        private HashMap<String, Order> all_orders;
        private UpdateCostFragment costFragment;
        private DeleteDishFragment deleteDishFragment;
        FragmentManager FmBase;

        public ExpandableListViewAdapter(Context context, List<String> tableOrdersList,
                                         HashMap<String, Order> all_orders, FragmentManager FmMain) {
            this._context = context;
            this.tableOrdersList = tableOrdersList;
            this.all_orders = all_orders;
            this.FmBase = FmMain;
        }

        @Override
        public int getGroupCount() {
            return this.tableOrdersList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.all_orders.get(this.tableOrdersList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.tableOrdersList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.all_orders.get(this.tableOrdersList.get(groupPosition)).get(childPosition);
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
            // Load table number and timeStamp of each order
            String table_number = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.order_title, null);
            }

            TextView orderInfo = convertView.findViewById(R.id.title_order);
            orderInfo.setText(table_number);

            TextView timeStamp = convertView.findViewById(R.id.textView_timeStamp);
            Calendar timestamp = all_orders.get(table_number).getTimeStamp();
            SimpleDateFormat formatter = new SimpleDateFormat(Database.FORMAT_TIME_STAMP);
            timeStamp.setText(formatter.format(timestamp.getTime()));



            return convertView;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // Load dish info for each order
            Dish dish = (Dish) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.order_topics_list, null);
            }

            TextView dish_number = convertView.findViewById(R.id.textView_dish_number);
            dish_number.setText((childPosition+1) + ")");

            EditText dish_name = convertView.findViewById(R.id.editText_dish_name);
            dish_name.setText(dish.getName());

            final EditText cost = convertView.findViewById(R.id.editText_cost);
            cost.setText("" + dish.getPrice());

            EditText description = convertView.findViewById(R.id.editText_description);
            description.setText(dish.getDescription());

            EditText notes = convertView.findViewById(R.id.editText_notes);
            notes.setText(dish.getNotes());

            Button editCostButton = convertView.findViewById(R.id.button_edit_cost);
            editCostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chapterTitle = (String) getGroup(groupPosition);
                    String table_number = chapterTitle.split(" ")[1];
                    costFragment = UpdateCostFragment.newInstance(cost);
                    Bundle bundle = new Bundle();
                    bundle.putString("table_number", table_number);
                    bundle.putInt("dish_position", childPosition);
                    costFragment.setArguments(bundle);
                    costFragment.show(FmBase, "UpdateCostFragment");
                }
            });

            Button deleteDishButton = convertView.findViewById(R.id.button_delete_dish);
            deleteDishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chapterTitle = (String) getGroup(groupPosition);
                    String table_number = chapterTitle.split(" ")[1];
                    deleteDishFragment = DeleteDishFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("table_number", table_number);
                    bundle.putInt("dish_position", childPosition);
                    bundle.putString("deleteFrom" , "live_orders");
                    deleteDishFragment.setArguments(bundle);
                    deleteDishFragment.show(FmBase, "DeleteDishFragment");
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}