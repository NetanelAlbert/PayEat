package com.example.payeat;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;

public class Order {
    private static Firebase firebaseReference = Database.getDataBaseInstance();

    private ArrayList<Dish> orderInfo; // order_info is collection of dishes
    private int table_number; // the identifier of the order.

    public Order(Dish[] order_info, int table_number) {
        this.orderInfo = new ArrayList<>();
        this.orderInfo.addAll(Arrays.asList(order_info));

        this.table_number = table_number;
    }

    public ArrayList<Dish> getOrderInfo() {
        return orderInfo;
    }

    public boolean add(Dish dish) {
        return orderInfo.add(dish);
    }

    public int size() {
        return this.orderInfo.size();
    }
    public Dish get(int childPosition) {
        return this.orderInfo.get(childPosition);
    }
}
