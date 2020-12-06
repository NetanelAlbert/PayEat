package com.example.payeat;

import android.widget.Toast;

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
    private static final Firebase.CompletionListener completionListener = new Firebase.CompletionListener() {
        @Override
        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
            if (firebaseError != null)
            {
//                            Toast.makeText(AddClientActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                System.out.println(firebaseError.getMessage());
            }
            else
            {
//                            Toast.makeText(AddClientActivity.this, "Saved!!", Toast.LENGTH_LONG).show();
                System.out.println("Saved!!");
            }
        }
    };

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
    public static String getPassword() {
        return dataSnapshot.child("password").getValue(String.class);
    }


    public static ArrayList<Order> getOrders() { // ido
        ArrayList<Order> result = new ArrayList<>();
        Iterable<DataSnapshot> order_iter = dataSnapshot.child("live_orders").getChildren();
        for (DataSnapshot order_snap: order_iter) {
            System.out.println("order key (table_number): -->" + order_snap.getKey());
            System.out.println("order value: -->" + order_snap.getValue());
            //get dishes
            ArrayList<Dish> dishesOfOrder = new ArrayList<>();
            Iterable<DataSnapshot> dish_iter = order_snap.child("dishes").getChildren();
            for (DataSnapshot dish_snap: dish_iter) {
                System.out.println("dish key: -->" + dish_snap.getKey());
                System.out.println("dish value: -->" + dish_snap.getValue());
                Dish temp_dish = dish_snap.getValue(Dish.class);
                dishesOfOrder.add(temp_dish);
            }
            int table_number = Integer.parseInt(order_snap.getKey());
            String timeFromDatabase = order_snap.child("time-stamp").getValue(String.class);
            Calendar timestamp = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            try {
                timestamp.setTime(sdf.parse(timeFromDatabase));// all done
            } catch (Exception e) {
                e.printStackTrace();
            }
            Order order = new Order(dishesOfOrder, table_number, timestamp);
            result.add(order);
        }
        return result;
    }


    public static String getCategoryNameByNumber(int id) {
        Iterable<DataSnapshot> categories_iter = dataSnapshot.child("menu").getChildren();
        int i=0;
        for (DataSnapshot category_snap: categories_iter) {
            if(i!=id)
                i++;
            else
                return category_snap.getKey();
        }
        return "error";
    }



    public static Order getOrder(int table_number) { // edut and eden
        return null;
    }


    public static Dish getDishFromMenu(String category, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child("menu").child(category).child(dish_id+"");
        return d.getValue(Dish.class);
    }

    public static Dish getDishFromOrders(int order_id, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child("live_orders").child(order_id+"").child("dishes").child(dish_id+"");
        return d.getValue(Dish.class);
    }

    public static ArrayList<String> getCategories() { // nati
        ArrayList<String> categories=new ArrayList<>();
        Iterable<DataSnapshot> categories_iter = dataSnapshot.child("menu").getChildren();
        for (DataSnapshot category_snap: categories_iter) {
               categories.add(category_snap.getKey());
        }
        return categories;
    }

    public static Menu getMenuByCategory(String category) { // edut
        ArrayList<Dish> dishes = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child("menu").child(category).getChildren();
        for (DataSnapshot dish_snap: dish_iter) {
            Dish temp_dish = dish_snap.getValue(Dish.class);
            dishes.add(temp_dish);
        }
        return new Menu(category,dishes);
    }


    public static boolean addDishToOrderInProgress(int table_number, Dish dish) {
        long numOfDishesInOrder = dataSnapshot.child("orders_in_progress").child(String.valueOf(table_number)).child("dishes").getChildrenCount();
        firebaseReference.child("orders_in_progress").child(String.valueOf(table_number)).child("dishes").child(String.valueOf(numOfDishesInOrder)).setValue(dish);
        return false;
    }
    public static boolean addDishToLiveOrder(int table_number, Dish dish) {
        long numOfDishesInOrder = dataSnapshot.child("live_orders").child(String.valueOf(table_number)).child("dishes").getChildrenCount();
        firebaseReference.child("live_orders").child(String.valueOf(table_number)).child("dishes").child(String.valueOf(numOfDishesInOrder)).setValue(dish);
        return false;
    }

    public static ArrayList<Dish> getOrderInProgress(int tableNum) {
        ArrayList<Dish> dishes = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child("orders_in_progress").child(String.valueOf(tableNum)).child("dishes").getChildren();
        for (DataSnapshot dish_snap : dish_iter) {
            System.out.println(dish_snap);
            Dish temp_dish = dish_snap.getValue(Dish.class);
            System.out.println(temp_dish.getName());
            dishes.add(temp_dish);
        }
        return dishes;

    }

    public static boolean deleteOrderFromProgress(int table_number) { // eden and ido
        firebaseReference.child("orders_in_progress").child("" + table_number).removeValue();
        return true;
    }
    public static boolean addOrderToLiveOrders(int table_number, ArrayList<Dish> dishes) { // eden and ido
        for (Dish dish: dishes){
            addDishToLiveOrder(table_number, dish);
        }
        return true;
    }

    public static boolean sendOrder(int table_number) { // eden and ido
        ArrayList<Dish> dishes =Database.getOrderInProgress(table_number);
        deleteOrderFromProgress(table_number);
        addOrderToLiveOrders(table_number,dishes);
        return true;
    }

    public static boolean deleteDishFromOrderInProgress(int order_id, int dish_id) { // eden and ido
        firebaseReference.child("orders_in_progress").child("" + order_id).child("dishes").child("" + dish_id).removeValue();
        return true;
    }

    public static boolean ChangeNotes(int dishPosition, String Notes, int tableNum) { // eden and ido
        //ArrayList<Dish> dishes = new ArrayList<>();
        System.out.println(dishPosition+"   "+Notes+"   "+tableNum);
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child("orders_in_progress").child(String.valueOf(tableNum)).child("dishes").getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child("orders_in_progress").child(tableNum+"").child("dishes").child(key).child("notes").getRef().setValue(Notes);
                break;
            }
            counter++;
        }
        
       // firebaseReference.child("orders_in_progress").child("" + tableNum).child("dishes").child("" + dish_id).removeValue();
        return true;
    }
    
    public static boolean appendOrder(Order order) {
        return false;
    }

    public static boolean deleteOrder(int order_id) {
        return false;
    }

    public static boolean setDishStock(Dish dish, boolean in_stock) {
        return false;
    }//manager

    public static boolean setPrice(String table_number, int dish_id, double new_price) {
        firebaseReference.child("live_orders").child(table_number).child("dishes").child(""+dish_id).child("price").setValue(new_price, completionListener);
        return true;
    }//manager

    public static boolean updateDishDetails(int dishPosition, Dish dish, String category) {
//        System.out.println(dishPosition+"   "+Notes+"   "+tableNum);
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child("menu").child(category).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child("menu").child(category).child(key).getRef().setValue(dish, completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }

    public static boolean addDishToMenuByCategory(Dish dish, String category) { // we have here a serious problem
        // everything seeing to be ok and the addition works but when we refresh the menu it throw null pointer exception
        long number_of_dishes = dataSnapshot.child("menu").child(category).getChildrenCount();
        System.out.println("key:-->" + dataSnapshot.child("menu").child(category).getKey());
        firebaseReference.child("menu").child(category).child(""+number_of_dishes).setValue(dish, completionListener);
        return true;
    } //manager

    /**
     * this function delete dish from the database
     * @param deleteFrom where? from menu or from live_orders
     * @param dishPosition the positon of the dish to delete in the list
     * @param focalize if delete from menu so focalize is category else if delete from is live_orders focalize is table_number
     * @return
     */
    public static boolean deleteDish(String deleteFrom ,int dishPosition, String focalize) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(deleteFrom).child(focalize).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child(deleteFrom).child(focalize).child(key).getRef().removeValue(completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }//manger
}
