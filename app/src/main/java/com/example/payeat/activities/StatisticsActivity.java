package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.payeat.R;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.interfaces.DataChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StatisticsActivity extends AppCompatActivity implements DataChangeListener {

    ListView listView_dailyProfit;
    ListView listView_dishCounter;

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
        ArrayList<String> daily_profit_list = Database.getDailyProfit();  // format: "date profit"
        //split and create adapter
        ArrayList<String> dish_counter = Database.getDishCounter();  // format: "dish_name count"
        //split and create adapter

        ArrayList<String> date_list = new ArrayList<>();
        ArrayList<Integer> profit_list = new ArrayList<>();
        for (String daily_profit: daily_profit_list) {
            String[] split = daily_profit.split(" ");
            date_list.add(split[0]);
            profit_list.add(Integer.parseInt(split[1]));
        }

        StatisticsActivity.CustomListAdapter<String, Integer> dailyProfitListAdapter = new StatisticsActivity.CustomListAdapter<String, Integer>(this, date_list, profit_list);
        listView_dailyProfit.setAdapter(dailyProfitListAdapter);

        ArrayList<String> dish_list = new ArrayList<>();
        ArrayList<Integer> counter_list = new ArrayList<>();
        for (String dish: dish_counter) {
            String[] split = dish.split(" ");
            dish_list.add(split[0]);
            counter_list.add(Integer.parseInt(split[1]));
        }

        StatisticsActivity.CustomListAdapter<String, Integer> capacityListAdapter = new StatisticsActivity.CustomListAdapter<String, Integer>(this, dish_list, counter_list);
        listView_dishCounter.setAdapter(capacityListAdapter);
    }


    class CustomListAdapter<T, K> extends ArrayAdapter<T> {

        Context context;
        ArrayList<T> subject_list;
        ArrayList<K> detail_list;

        CustomListAdapter (Context context, ArrayList<T> subject_list, ArrayList<K> detail_list) {
            super(context, R.layout.statistic_list_row, subject_list);
            this.context = context;
            this.subject_list = subject_list;
            this.detail_list = detail_list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.statistic_list_row, parent, false);

            TextView subject = row.findViewById(R.id.textView_subject);
            subject.setText(subject_list.get(position).toString());

            TextView detail = row.findViewById(R.id.textView_detail);
            detail.setText(detail_list.get(position).toString());

            return row;
        }

    }
}