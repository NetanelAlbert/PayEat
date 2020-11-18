package com.example.payeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.example.payeat.fragments.ChooseTableFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistOrdersActivity extends AppCompatActivity {

    private ExpandableListViewAdapter listViewAdapter;
    private ExpandableListView expandableListView;
    private SearchView searchView;
    private ChooseTableFragment chooseTableFragment;

    private List<String> idOrderList;
    private HashMap<String, Order> all_orders; // order_id -> Order/Dishes

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
                        startActivity(new Intent(getApplicationContext(), ManagerMenuActivity.class));
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
        makeList();
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

    private void makeList() {
        idOrderList = new ArrayList<String>(); // id Order (the title of the Order whatever)
        all_orders = new HashMap<String, Order>(); // each raw in the Order. what is the Order info.

        ArrayList<Order> orders = new ArrayList<>();

        // when firebase will be ready hear we are going to do the quary

        Dish dish1 = new Dish("שורה 1", 50, "description1");
        Dish dish2 = new Dish("שורה 2", 40, "description2");
        Dish dish3 = new Dish("שורה 3", 30, "description3");

        Order order1 = new Order(new Dish[]{dish1, dish2, dish3}, 1);
        Order order2 = new Order(new Dish[]{dish2, dish3, dish1}, 2);
        Order order3 = new Order(new Dish[]{dish3, dish1, dish2}, 3);

        idOrderList.add("שולחן 1");
        idOrderList.add("שולחן 2");
        idOrderList.add("שולחן 3");

        all_orders.put(idOrderList.get(0), order1);
        all_orders.put(idOrderList.get(1), order2);
        all_orders.put(idOrderList.get(2), order3);
    }
}