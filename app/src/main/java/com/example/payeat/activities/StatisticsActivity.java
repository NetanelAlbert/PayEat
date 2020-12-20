package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class StatisticsActivity extends AppCompatActivity implements DataChangeListener {

    ListView listView_dailyProfit;
    ListView listView_dishCounter;
    private SearchView SearchView_dailyProfit;
    private SearchView SearchView_dishCounter;

    ArrayList<String> date_list = new ArrayList<>();
    ArrayList<Integer> profit_list = new ArrayList<>();

    ArrayList<String> dish_list = new ArrayList<>();
    ArrayList<Integer> counter_list = new ArrayList<>();

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

        notifyOnChange();
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
        ArrayList<String[]> daily_profit_list = Database.getDailyProfit();  // format: {"date", "profit"}
        //split and create adapter
        ArrayList<String[]> dish_counter = Database.getDishCounter();  // format: {"dish_name", count"}
        //split and create adapter

        for (String[] daily_profit: daily_profit_list) {
            date_list.add(daily_profit[0]);
            profit_list.add(Integer.parseInt(daily_profit[1]));
        }

        StatisticsActivity.CustomListAdapter<String, Integer> dailyProfitListAdapter = new StatisticsActivity.CustomListAdapter<>(this, date_list, profit_list, R.layout.daily_profit_row);
        listView_dailyProfit.setAdapter(dailyProfitListAdapter);

        for (String[] dish: dish_counter) {
            dish_list.add(dish[0]);
            counter_list.add(Integer.parseInt(dish[1]));
        }

        StatisticsActivity.CustomListAdapter<String, Integer> capacityListAdapter = new StatisticsActivity.CustomListAdapter<>(this, dish_list, counter_list, R.layout.dish_counter_row);
        listView_dishCounter.setAdapter(capacityListAdapter);
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