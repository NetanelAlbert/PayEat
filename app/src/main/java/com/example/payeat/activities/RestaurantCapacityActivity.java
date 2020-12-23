//
// Author: Ido Shapira
//
// This activity is shown right after the manager decided he wants to see the current capacity of the restaurant on the app.
// He gets a list of all the tables in the restaurant
// and next to each table the status of that table (available, occupied, ask for bill).

package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.interfaces.DataChangeListener;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.dataObjects.Order;
import com.example.payeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;

public class RestaurantCapacityActivity extends AppCompatActivity implements DataChangeListener {

    ListView listView_capacity;

    ArrayList<String> table_number_list;
    ArrayList<String> is_occupied_list;

    int max_tables_number;
    MenuItem update_max_tables_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_capacity);

        // Setup bottom navigation view --> (menu, orders, capacity)
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

        // Get all order from database and build table_number_list and is_occupied_list
        notifyOnChange();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_capacity, menu);
        update_max_tables_item = menu.getItem(0);
        max_tables_number = Database.getMaxTableNumber();
        update_max_tables_item.setTitle("שנה כמות שולחנות זמינים\n זמין: " + max_tables_number);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        CreateUpdateTablesNumberDialog();
        return true;
    }

    private void CreateUpdateTablesNumberDialog() {
        final Dialog edit_max_number_tables_dialog = new Dialog(this);
        edit_max_number_tables_dialog.setContentView(R.layout.change_number_of_statitstics_items);
        edit_max_number_tables_dialog.setTitle("edit max number tables");
        edit_max_number_tables_dialog.setCancelable(true);

        final EditText editText_newDisplayNumber = edit_max_number_tables_dialog.findViewById(R.id.editTextNumber_new_display_number);
        final Button OKbutton = edit_max_number_tables_dialog.findViewById(R.id.button_update);
        Button Canclebutton = edit_max_number_tables_dialog.findViewById(R.id.button_cancel);

        OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_display_numberS = editText_newDisplayNumber.getText().toString();
                if(new_display_numberS == null || new_display_numberS.length() == 0)
                    return;
                max_tables_number = Integer.parseInt(new_display_numberS);
                Database.setMaxTableNumber(max_tables_number);
                edit_max_number_tables_dialog.dismiss();
            }
        });

        editText_newDisplayNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                OKbutton.callOnClick();
                return true;
            }
        });

        Canclebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_max_number_tables_dialog.dismiss();
            }
        });
        edit_max_number_tables_dialog.show();

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
        max_tables_number = Database.getMaxTableNumber();
        table_number_list.clear();
        for(int i=1; i<=max_tables_number+1; i++) {
            table_number_list.add("שולחן " + i);
        }
        is_occupied_list.clear();
        for(int i=1; i<=max_tables_number+1; i++) {
            is_occupied_list.add("פנוי");
        }
        ArrayList<Order> orders_in_progress = Database.getOrders("orders_in_progress");
        ArrayList<Order> orders_in_live_orders = Database.getOrders("live_orders");

        HashSet<Order> hashSet = new HashSet<>();
        hashSet.addAll(orders_in_progress);
        hashSet.addAll(orders_in_live_orders);
        ArrayList<Order> orders = new ArrayList<>(hashSet);

        for (Order order: orders) {
            int table_number = order.getTable_number();
            is_occupied_list.set(table_number -1 , "תפוס");
        }

        ArrayList<Order> orders_in_askBill =Database.getOrders("ask_bill");
        for (Order order: orders_in_askBill) {
            int table_number = order.getTable_number();
            is_occupied_list.set(table_number -1 , "הזמינו חשבון");
        }

        CapacityListAdapter capacityListAdapter = new CapacityListAdapter(this, table_number_list, is_occupied_list);
        listView_capacity.setAdapter(capacityListAdapter);
    }

    class CapacityListAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rtable_number_list;
        ArrayList<String> ris_occupied_list;

        CapacityListAdapter (Context context, ArrayList<String> title, ArrayList<String> description) {
            super(context, R.layout.capacity_row, title);
            this.context = context;
            this.rtable_number_list = title;
            this.ris_occupied_list = description;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Load the status of each table (the default is 'free')
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.capacity_row, parent, false);

            TextView myTitle = row.findViewById(R.id.textView_table_id);
            myTitle.setText(rtable_number_list.get(position));

            TextView is_occupied = row.findViewById(R.id.textView_occupied);
            is_occupied.setText(ris_occupied_list.get(position));
            if(ris_occupied_list.get(position).compareTo("תפוס") == 0) {
                is_occupied.setTextColor(Color.RED);
            }
            else if(ris_occupied_list.get(position).compareTo("הזמינו חשבון") == 0) {
                is_occupied.setTextColor(Color.rgb(33, 150, 243));
            }
            Button freeTable =  row.findViewById(R.id.release_table_button);
            System.out.println("freeTable");
            if(ris_occupied_list.get(position).compareTo("הזמינו חשבון") != 0) {
                freeTable.setVisibility(View.INVISIBLE);

            }
            else{
                freeTable.setVisibility(View.VISIBLE);
                freeTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date;
                        //todo orderPrice=realPrice
                        Database.freeTable(position);
                        Toast.makeText(getContext(), "שיחררתי!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return row;
        }
    }
}