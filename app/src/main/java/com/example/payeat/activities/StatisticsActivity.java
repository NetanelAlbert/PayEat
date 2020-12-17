package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.payeat.R;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.interfaces.DataChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticsActivity extends AppCompatActivity implements DataChangeListener {

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

    }
}