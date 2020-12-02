package com.example.payeat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    public static Firebase getDataBaseInstance() { return firebaseReference;}

    public static String getManagerName() {
        return dataSnapshot.child("manager_name").getValue(String.class);
    }

    public static String getRestaurantName() {
        return dataSnapshot.child("restaurant_name").getValue(String.class);
    }

    public static int getMaxTableNumber() {
        return dataSnapshot.child("max_table_number").getValue(Integer.class);
    }

    public static ArrayList<Order> getOrders() { // ido
        ArrayList<Order> result = new ArrayList<>();
        Iterable<DataSnapshot> order_iter = dataSnapshot.child("live_orders").getChildren();
        for (DataSnapshot order_snap: order_iter) {
            System.out.println("order key: -->" + order_snap.getKey());
            System.out.println("order value: -->" + order_snap.getValue());
            //get dishes
            ArrayList<Dish> dishesOfOrder = new ArrayList<>();
            Iterable<DataSnapshot> dish_iter = order_snap.child("dishes").getChildren();
            for (DataSnapshot dish_snap: dish_iter) {
                System.out.println("dish key: -->" + dish_snap.getKey());
                System.out.println("dish value: -->" + dish_snap.getValue());

//                Object dish_from_database = dish_snap.getValue(List<Object>.class);

//                Dish temp_dish = dish_snap.getValue(Dish.class);
                Dish temp_dish = convertDataSnapShotToDish(dish_snap);
                dishesOfOrder.add(temp_dish);
            }
            int table_number = order_snap.child("table_number").getValue(Integer.class);
            String timeFromDatabase = order_snap.child("timestamp").getValue(String.class);
            Calendar timestamp = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            try {
                timestamp.setTime(sdf.parse(timeFromDatabase));// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Order order = new Order(dishesOfOrder, table_number, timestamp);
            result.add(order);
        }
        return result;
    }

    public static ArrayList<Dish> getDishesByCategory(String category) {
        ArrayList<Dish> result = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child("menu").child(category).getChildren();
        for (DataSnapshot dish_snap: dish_iter) {
                Dish temp_dish = convertDataSnapShotToDish(dish_snap);
                result.add(temp_dish);
            }
        return result;
    }


    public static Order getOrder(int table_number) { // edut and eden
        return null;
    }


    public static Dish getDishFromMenu(String category, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child("menu").child(category).child(dish_id+"");
        return convertDataSnapShotToDish(d);
    }

    public static Dish getDishFromOrders(int order_id, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child("live_orders").child(order_id+"").child("dishes").child(dish_id+"");
        return convertDataSnapShotToDish(d);
    }

    public static ArrayList<String> getCategories() { // nati
        return null;
    }

    public static Menu getMenuByCategory(String category) { // edut
        return null;
    }

    public static boolean addDishToOrder(int table_number, Dish dish) { // edut and ido
        return false;
    }

    public static boolean deleteDishFromOrder(int table_number, Dish dish) { // eden and ido
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

    private static Dish convertDataSnapShotToDish(DataSnapshot dish_snap) {
        String description = dish_snap.child("description").getValue(String.class);
        long dishID = dish_snap.child("dishID").getValue(Long.class);
        boolean in_stock = dish_snap.child("in_stock").getValue(Boolean.class);
        String name = dish_snap.child("name").getValue(String.class);
        String notes = dish_snap.child("notes").getValue(String.class);
        int price = dish_snap.child("price").getValue(Integer.class);
        int shares = dish_snap.child("shares").getValue(Integer.class);

        return new Dish(dishID, name, price, description, in_stock, shares, notes);

    }

}
