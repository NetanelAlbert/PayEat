package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistOrdersActivity extends AppCompatActivity {

    private ExpandableListViewAdapter listViewAdapter;
    private ExpandableListView expandableListView;
    private SearchView searchView;
    private List<String> orderList;
    private HashMap<String, List<String>> all_orders; // order_id -> order

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exist_orders);

        expandableListView = findViewById(R.id.listView_orders);
        makeList();
        listViewAdapter = new ExpandableListViewAdapter(this, orderList, all_orders);
        expandableListView.setAdapter(listViewAdapter);

    }

    private void makeList() {
        orderList = new ArrayList<String>(); // id order (the title of the order whatever)
        all_orders = new HashMap<String, List<String>>(); // each raw in the order. what is the order info.
        int number_of_orders = 3;

        // when firebase will be ready hear we are going to do the quary

        for(int i= 1; i<=number_of_orders; i++) {
            orderList.add("Order "+ i);
        }
        List<String> order1 = new ArrayList<>(); // in the future it will be an object 'Order'
        order1.add("Raw 1");
        order1.add("Raw 2");
        order1.add("Raw 3");

        List<String> order2 = new ArrayList<>(); // in the future it will be an object 'Order'
        order2.add("Raw 4");
        order2.add("Raw 5");
        order2.add("Raw 6");

        List<String> order3 = new ArrayList<>(); // in the future it will be an object 'Order'
        order3.add("Raw 7");
        order3.add("Raw 8");
        order3.add("Raw 9");

        all_orders.put(orderList.get(0), order1);
        all_orders.put(orderList.get(1), order2);
        all_orders.put(orderList.get(2), order3);
    }

    private void initSearchWidgets()
    {
        searchView = (SearchView) findViewById(R.id.SearchView_orders);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
//                ArrayList<Order> filteredOrders = new ArrayList<Order>();
                ArrayList<String> filteredOrders = new ArrayList<String>();

                for(String order: orderList)
                {
                    if(order.toLowerCase().contains(s.toLowerCase()))
                    {
                        filteredOrders.add(order);
                    }
                }

//                for(Order order: orderList)
//                {
//                    if(order.getName().toLowerCase().contains(s.toLowerCase()))
//                    {
//                          filteredShapes.add(order);
//                    }
//                }
                listViewAdapter = new ExpandableListViewAdapter(getApplicationContext(), filteredOrders, all_orders);
                expandableListView.setAdapter(listViewAdapter);
                return false;
            }
        });
    }


//    ListView OrderListView;
//    String[] orders;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_exist_orders);
//
//        OrderListView = (ListView) findViewById(R.id.listView_orders);
//        orders = new DateFormatSymbols().getMonths();
////        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orders);
//        ArrayAdapter<String> ordersAdapter = new ArrayAdapter<>(this, R.layout.list_item, orders);
//        OrderListView.setAdapter(ordersAdapter);
//        OrderListView.setOnItemClickListener(this);
//
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String order = parent.getItemAtPosition(position).toString();
//        Toast.makeText(getApplicationContext(), "Clicked: "+ order, Toast.LENGTH_SHORT).show();
//    }
}