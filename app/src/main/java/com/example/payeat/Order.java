package com.example.payeat;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;

public class Order {
    private static Firebase firebaseReference = Database.getDataBaseInstance();

    private int order_id;
    private ArrayList<Dish> orderInfo; // order_info is collection of dishes
    private int table_number;
    public ArrayList<Dish> getOrderInfo() {
        return orderInfo;
    }

    public Order(Dish[] order_info) {
        this.orderInfo = new ArrayList<>();
        this.orderInfo.addAll(Arrays.asList(order_info));

        // Get order_id from the database
        this.order_id = getOrderId() + 1; // TODO: fix! not working yet
        setOrderId(this.order_id);

        // Get table_number from the database
        this.table_number = getTableNumber();
    }

    public boolean add(Dish dish) {
        return orderInfo.add(dish);
    }

    public static int getOrderId() {
//        final int[] previous_id = {-1};
//        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                previous_id[0] = dataSnapshot.child("number_of_orders").getValue(Integer.class);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) { }
//
//        });
//        return previous_id[0];
        return -1; // this is not working
    }

    public static void setOrderId(int new_orderId) {
        firebaseReference.child("number_of_orders").setValue(new_orderId);
    }

    public int getTableNumber() {
        return 1;
    }


    public int size() {
        return this.orderInfo.size();
    }
    public Dish get(int childPosition) {
        return this.orderInfo.get(childPosition);
    }
}
