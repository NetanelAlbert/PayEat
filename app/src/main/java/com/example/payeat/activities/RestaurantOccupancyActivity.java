package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.DataChangeListener;
import com.example.payeat.Database;
import com.example.payeat.Order;
import com.example.payeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RestaurantOccupancyActivity extends AppCompatActivity implements DataChangeListener {

    ListView listView_capacity;

    ArrayList<String> table_number_list;
    ArrayList<String> is_occupied_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_occupancy);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.restaurant_capacity);
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
                        startActivity(new Intent(getApplicationContext(), ExistOrdersActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.restaurant_capacity:
                        return true;
                }
                return false;
            }
        });

        listView_capacity = findViewById(R.id.listView_capacity);

        table_number_list = new ArrayList<>();
        is_occupied_list = new ArrayList<>();
        notifyOnChange();

//        int max_table_number = Database.getMaxTableNumber();
//        table_number_list = new ArrayList<>();
//        for(int i=1; i<=max_table_number; i++) {
//            table_number_list.add("שולחן " + i);
//        }
//        is_occupied_list = new ArrayList<>();
//        for(int i=1; i<=max_table_number; i++) {
//            is_occupied_list.add("פנוי");
//        }
//        ArrayList<Order> orders = Database.getOrders();
//        for (Order order: orders) {
//            int table_number = order.getTable_number();
//            is_occupied_list.set(table_number -1 , "תפוס");
//        }

        CapacityListAdapter capacityListAdapter = new CapacityListAdapter(this, table_number_list, is_occupied_list);
        listView_capacity.setAdapter(capacityListAdapter);
        listView_capacity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(RestaurantOccupancyActivity.this, "click", Toast.LENGTH_SHORT).show();
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
        int max_table_number = Database.getMaxTableNumber();
        System.out.println("max_table_number -->" + max_table_number);
        table_number_list.clear();
        for(int i=1; i<=max_table_number; i++) {
            table_number_list.add("שולחן " + i);
        }
        is_occupied_list.clear();
        for(int i=1; i<=max_table_number; i++) {
            is_occupied_list.add("פנוי");
        }
        ArrayList<Order> orders = Database.getOrders();
        for (Order order: orders) {
            int table_number = order.getTable_number();
            is_occupied_list.set(table_number -1 , "תפוס");
        }
    }

    class CapacityListAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rtable_number_list;
        ArrayList<String> ris_occupied_list;

        CapacityListAdapter (Context context, ArrayList<String> title, ArrayList<String> description) {
            super(context, R.layout.capacity_row, R.id.title_order, title);
            this.context = context;
            this.rtable_number_list = title;
            this.ris_occupied_list = description;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.capacity_row, parent, false);

            TextView myTitle = row.findViewById(R.id.textView_table_id);
            myTitle.setText(rtable_number_list.get(position));

            TextView is_occupied = row.findViewById(R.id.textView_occupied);
            is_occupied.setText(ris_occupied_list.get(position));
            if(ris_occupied_list.get(position).compareTo("תפוס") == 0) {
                is_occupied.setTextColor(Color.parseColor("red"));
            }

            return row;
        }
    }
}