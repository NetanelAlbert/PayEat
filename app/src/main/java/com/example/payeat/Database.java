package com.example.payeat;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Database extends android.app.Application implements ValueEventListener {
    private static Firebase firebaseReference;
    private static DataSnapshot dataSnapshot;
    private static ArrayList<DataChangeListener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");
        firebaseReference.addValueEventListener(this);
        listeners = new ArrayList<>();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Database.dataSnapshot = dataSnapshot;
        notifyAllListeners();
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        //TODO handle error

    }

    public static void addListener(DataChangeListener listener){
        listeners.add(listener);
    }

    public static boolean removeListener(DataChangeListener listener){
        return listeners.remove(listener);
    }

    private static void notifyAllListeners(){
        for (DataChangeListener listener : listeners) {
            listener.notifyOnChange();
        }
    }

    public static String getManagerName() {
        return dataSnapshot.child("manager_name").getValue(String.class);
    }

    public static String getRestaurantName() {
        return dataSnapshot.child("restaurant_name").getValue(String.class);
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
