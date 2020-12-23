package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.payeat.R;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.dataObjects.Order;
import com.example.payeat.interfaces.DataChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class StatisticsActivity extends AppCompatActivity implements DataChangeListener {

    ListView listView_dailyProfit;
    ListView listView_dishCounter;
    SearchView SearchView_dailyProfit;
    SearchView SearchView_dishCounter;

    ArrayList<String> date_list = new ArrayList<>();
    ArrayList<Integer> profit_list = new ArrayList<>();

    ArrayList<String> dish_list = new ArrayList<>();
    ArrayList<Integer> counter_list = new ArrayList<>();

    int to_display = 5;
    MenuItem display_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Setup bottom navigation view --> (menu, orders, capacity, statistics)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.statistics);
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
                        startActivity(new Intent(getApplicationContext(), RestaurantCapacityActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
//                    case R.id.statistics:
//                        return true;
                }
                return false;
            }
        });

        listView_dailyProfit = findViewById(R.id.ListView_dailyProfit);
        listView_dishCounter = findViewById(R.id.ListView_dishCounter);

        // Setup search view --> search by date
        SearchView_dailyProfit = findViewById(R.id.SearchView_dailyProfit);
        SearchView_dailyProfit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> date_list_filtered = new ArrayList<>();
                ArrayList<Integer> profit_list_filtered = new ArrayList<>();

                for(int i = 0; i< date_list.size(); i++) {
                    String date = date_list.get(i);
                    if(date.toLowerCase().contains(newText.toLowerCase()))
                    {
                        date_list_filtered.add(date);
                        profit_list_filtered.add(profit_list.get(i));
                    }
                }
                StatisticsActivity.CustomListAdapter<String, Integer> dailyProfitListAdapter =
                        new StatisticsActivity.CustomListAdapter<>(getApplicationContext(), date_list_filtered, profit_list_filtered, R.layout.daily_profit_row);
                listView_dailyProfit.setAdapter(dailyProfitListAdapter);
                return false;
            }
        });

        // Setup search view --> search by dish name
        SearchView_dishCounter = findViewById(R.id.SearchView_dishCounter);
        SearchView_dishCounter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> dish_list_filtered = new ArrayList<>();
                ArrayList<Integer> count_list_filtered = new ArrayList<>();

                for(int i = 0; i< dish_list.size(); i++) {
                    String dish = dish_list.get(i);
                    if(dish.toLowerCase().contains(newText.toLowerCase()))
                    {
                        dish_list_filtered.add(dish);
                        count_list_filtered.add(counter_list.get(i));
                    }
                }
                StatisticsActivity.CustomListAdapter<String, Integer> dishCounterListAdapter =
                        new StatisticsActivity.CustomListAdapter<>(getApplicationContext(), dish_list_filtered, count_list_filtered, R.layout.dish_counter_row);
                listView_dishCounter.setAdapter(dishCounterListAdapter);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        display_item = menu.getItem(0);
        display_item.setTitle("שנה כמות לתצוגה\n מציג: " + to_display);
        onOptionsItemSelected(display_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        CreateDisplayNumberDialog();
        return true;
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

    private void CreateDisplayNumberDialog() {
        final Dialog edit_display_number_dialog = new Dialog(this);
        edit_display_number_dialog.setContentView(R.layout.change_number_of_statitstics_items);
        edit_display_number_dialog.setTitle("edit display number");
        edit_display_number_dialog.setCancelable(true);

        final EditText editText_newDisplayNumber = edit_display_number_dialog.findViewById(R.id.editTextNumber_new_display_number);
        final Button OKbutton = edit_display_number_dialog.findViewById(R.id.button_update);
        Button Canclebutton = edit_display_number_dialog.findViewById(R.id.button_cancel);

        OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_display_numberS = editText_newDisplayNumber.getText().toString();
                if(new_display_numberS == null || new_display_numberS.length() == 0)
                    return;
                to_display = Integer.parseInt(new_display_numberS);
                notifyOnChange();
                edit_display_number_dialog.dismiss();
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
                edit_display_number_dialog.dismiss();
            }
        });
        edit_display_number_dialog.show();
    }
    @Override
    public void notifyOnChange() {
        ArrayList<String[]> daily_profit_list = Database.getDailyProfit();  // format: {"date", "profit"}
        //split and create adapter
        ArrayList<String[]> dish_counter = Database.getDishCounter();  // format: {"dish_name", count"}
        //split and create adapter

        date_list.clear();
        profit_list.clear();
        dish_list.clear();
        counter_list.clear();

        for (String[] daily_profit: daily_profit_list) {
            boolean added = false;
            int profit = Integer.parseInt(daily_profit[1]);
            for(int i=0; i<date_list.size() && !added; i++) {
                if(profit > profit_list.get(i)) {
                    date_list.add(i, daily_profit[0]);
                    profit_list.add(i, profit);
                    added = true;
                }
            }
            if(!added) {
                date_list.add(daily_profit[0]);
                profit_list.add(profit);
            }
        }

        StatisticsActivity.CustomListAdapter<String, Integer> dailyProfitListAdapter = new StatisticsActivity.CustomListAdapter<>(this, date_list, profit_list, R.layout.daily_profit_row);
        listView_dailyProfit.setAdapter(dailyProfitListAdapter);

        for (String[] dish: dish_counter) {
            boolean added = false;
            int amount = Integer.parseInt(dish[1]);
            for(int i=0; i<dish_list.size() && !added; i++) {
                if(amount > counter_list.get(i)) {
                    dish_list.add(i, dish[0]);
                    counter_list.add(i, amount);
                    added = true;
                }
            }
            if(!added) {
                dish_list.add(dish[0]);
                counter_list.add(amount);
            }
        }

        while (date_list.size() > to_display) {
            int index = date_list.size() -1; // last
            date_list.remove(index);
            profit_list.remove(index);
        }

        while (dish_list.size() > to_display) {
            int index = dish_list.size() -1; // last
            dish_list.remove(index);
            counter_list.remove(index);
       }


        StatisticsActivity.CustomListAdapter<String, Integer> capacityListAdapter = new StatisticsActivity.CustomListAdapter<>(this, dish_list, counter_list, R.layout.dish_counter_row);
        listView_dishCounter.setAdapter(capacityListAdapter);

        display_item.setTitle("שנה כמות לתצוגה\n מציג: " + to_display);
    }


    class CustomListAdapter<T, K> extends ArrayAdapter<T> {

        Context context;
        ArrayList<T> subject_list;
        ArrayList<K> detail_list;
        int layout;

        CustomListAdapter (Context context, ArrayList<T> subject_list, ArrayList<K> detail_list, final int layout) {
            super(context, layout, subject_list);
            this.layout = layout;
            this.context = context;
            this.subject_list = subject_list;
            this.detail_list = detail_list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(layout, parent, false);

            TextView subject = row.findViewById(R.id.textView_subject);
            subject.setText(subject_list.get(position).toString());

            TextView detail = row.findViewById(R.id.textView_detail);
            detail.setText(detail_list.get(position).toString());

            return row;
        }

    }
}