package com.example.payeat;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Database extends android.app.Application {

    private static Firebase firebaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");
    }
    public static Firebase getDataBaseInstance() { return firebaseReference;}

    public static ArrayList<Order> getOrders() {
        return null;
    }

    public static Order getOrder(int order_id) {
        return null;
    }

    public static Dish getDish(int dish_id) {
        return null;
    }

    public static ArrayList<String> getCategories() {
        return null;
    }

    public static Menu getMenuByCategory(String category) {
        return null;
    }

    public static boolean addDishToOrder(int order_id, Dish dish) {
        return false;
    }

    public static boolean deleteDishFromOrder(int order_id, Dish dish) {
        return false;
    }

    public static boolean appendOrder(Order order) {
        return false;
    }

    public static boolean deleteOrder(int order_id) {
        return false;
    }

    public static boolean setDishStock(Dish dish, boolean in_stock) {
        return false;
    }

    public static boolean setPrice(Dish dish, int new_price) { //TODO maybe to remove this option
        return false;
    }

    public static boolean addDishToMenuByCategory(Dish dish, String category) {
        return false;
    }

    public static boolean deleteDishFromMenuByCategory(Dish dish, String category) {
        return false;
    }


}
