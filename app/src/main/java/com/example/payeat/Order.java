package com.example.payeat;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class Order {

    private ArrayList<Dish> orderInfo; // order_info is collection of dishes
    private int table_number; // the identifier of the order.
    private Calendar timeStamp;

    public Order(ArrayList<Dish> orderInfo, int table_number, Calendar timeStamp) {
        this.orderInfo = new ArrayList<>();
        this.orderInfo.addAll(orderInfo);
        this.table_number = table_number;
        this.timeStamp = timeStamp;
    }

    public Order(ArrayList<Dish> dishes, int table_number) {
        this.orderInfo = new ArrayList<>();
        this.orderInfo.addAll(dishes);
        this.table_number = table_number;
        this.timeStamp = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getTable_number() == order.getTable_number();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTable_number());
    }

    public ArrayList<Dish> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(ArrayList<Dish> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int size() {
        return this.orderInfo.size();
    }

    public Dish get(int childPosition) {
        return this.orderInfo.get(childPosition);
    }

    public Dish deleteDish(int index) {
        return orderInfo.remove(index);
    }

    public int calculateOrder() {
        int sum = 0;
        for (Dish d: this.orderInfo) {
            sum += d.getPrice();
        }
        return sum;
    }


}
