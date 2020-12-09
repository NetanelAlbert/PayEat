package com.example.payeat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Database extends android.app.Application implements ValueEventListener {
    private static Firebase firebaseReference;
    private static DataSnapshot dataSnapshot;
    private static ArrayList<DataChangeListener> listeners;


    // public constants
    public static final String DISHES = "dishes";
    public static final String LIVE_ORDERS = "live_orders";
    public static final String ORDERS_IN_PROGRESS = "orders_in_progress";
    public static final String MANAGER_NAME = "manager_name";
    public static final String RESTAURANT_NAME = "restaurant_name";
    public static final String PASSWORD = "password";
    public static final String MAX_TABLE_NUMBER = "max_table_number";
    public static final String TIME_STAMP = "time-stamp";
    public static final String PRICE = "price";
    public static final String NOTES = "notes";
    public static final String FORMAT_TIME_STAMP = "dd/MM/yyyy - HH:mm:ss";
    public static final String SRC_NAME = "src name";
    public static final String AskBill = "ask_bill";





    // private constants
    private static final String MENU = "menu";


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

    public static ArrayList<Dish> getLiveOrder(int tableNum) {
        ArrayList<Dish> dishesArray = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(LIVE_ORDERS).child(String.valueOf(tableNum)).child(DISHES).getChildren();
        for (DataSnapshot dish_snap : dish_iter) {
            Dish temp_dish = dish_snap.getValue(Dish.class);
            dishesArray.add(temp_dish);
        }
        return dishesArray;
    }

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);
        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");
        firebaseReference.addValueEventListener(this);
        listeners = new ArrayList<>();

        super.onCreate();
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
        return dataSnapshot.child(MANAGER_NAME).getValue(String.class);
    }

    public static String getRestaurantName() {
        return dataSnapshot.child(RESTAURANT_NAME).getValue(String.class);
    }

    public static int getMaxTableNumber() {
        return dataSnapshot.child(MAX_TABLE_NUMBER).getValue(Integer.class);
    }
    public static String getPassword() {
        return dataSnapshot.child(PASSWORD).getValue(String.class);
    }
    public static String getRestaurantLogoURL(){
        //return dataSnapshot.child(MAIN_MENU_PICTURES).child(menuName).getValue(String.class);
        return dataSnapshot.child(IMAGE_URL).getValue(String.class);
    }


    public static ArrayList<Order> getOrders(String fromWhere) { // ido
        ArrayList<Order> result = new ArrayList<>();
        Iterable<DataSnapshot> order_iter = dataSnapshot.child(fromWhere).getChildren();
        for (DataSnapshot order_snap: order_iter) {
            ArrayList<Dish> dishesOfOrder = new ArrayList<>();
            Iterable<DataSnapshot> dish_iter = order_snap.child(DISHES).getChildren();
            for (DataSnapshot dish_snap: dish_iter) {
                Dish temp_dish = dish_snap.getValue(Dish.class);
                dishesOfOrder.add(temp_dish);
            }
            int table_number = Integer.parseInt(order_snap.getKey());
            String timeFromDatabase = order_snap.child(TIME_STAMP).getValue(String.class);
            Calendar timestamp = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME_STAMP);
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




    public static Dish getDishFromMenu(String category, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child(MENU).child(category).child(DISHES).child(dish_id+"");
        return d.getValue(Dish.class);
    }

    public static Dish getDishFromOrders(int order_id, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child(LIVE_ORDERS).child(order_id+"").child(DISHES).child(dish_id+"");
        return d.getValue(Dish.class);
    }

    public static ArrayList<String> getCategories() { // nati
        ArrayList<String> categories=new ArrayList<>();
        Iterable<DataSnapshot> categories_iter = dataSnapshot.child(MENU).getChildren();
        for (DataSnapshot category_snap: categories_iter) {
               categories.add(category_snap.getKey());
        }
        return categories;
    }
    public static boolean addDishToLiveOrder(int table_number, Dish dish, int id) {
        firebaseReference.child(LIVE_ORDERS).child(table_number + "").child(DISHES).child(id+"").setValue(dish);
        return true;

    }


    public static boolean addOrderToLiveOrders(int table_number, ArrayList<Dish> dishesArray) { // eden and ido
        int id;
        if(dataSnapshot.child(LIVE_ORDERS).child(""+ table_number).child(DISHES).getChildrenCount()==0)
            id=0;
        else
            id=getMaxId(table_number);
        for (Dish dish: dishesArray){
            addDishToLiveOrder(table_number, dish, id);
            id++;
        }
        Calendar calendar = Calendar.getInstance(); // Returns instance with current date and time set
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_TIME_STAMP);
        dataSnapshot.child(LIVE_ORDERS).child(table_number + "").child(TIME_STAMP).getRef().setValue(formatter.format(calendar.getTime()));
        return true;
    }

    private static int getMaxId(int table_number) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(LIVE_ORDERS).child(table_number + "").child(DISHES).getChildren();
        long numOfDishes = dataSnapshot.child(LIVE_ORDERS).child(table_number + "").child(DISHES).getChildrenCount();
        int counter = 0;
        for (DataSnapshot dish_snap : dish_iter) {
            if (counter == numOfDishes - 1) {
                String key = dish_snap.getKey();
                int newKey = Integer.parseInt(key) + 1;
                return newKey;
            }
            counter++;
        }
        return -1;
    }
    private static final String IMAGE_URL = "img_url";
    public static Menu getMenuByCategory(String category) { // edut
        ArrayList<Dish> dishesArray = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).child(DISHES).getChildren();
        for (DataSnapshot dish_snap: dish_iter) {
                Dish temp_dish = dish_snap.getValue(Dish.class);
                dishesArray.add(temp_dish);
        }
        return new Menu(category,dishesArray);
    }


    public static boolean addDishToOrderInProgress(int table_number, Dish dish) {
        long numOfDishesInOrder = dataSnapshot.child(ORDERS_IN_PROGRESS).child(String.valueOf(table_number)).child(DISHES).getChildrenCount();
        firebaseReference.child(ORDERS_IN_PROGRESS).child(String.valueOf(table_number)).child(DISHES).child(String.valueOf(numOfDishesInOrder)).setValue(dish);
        Calendar calendar = Calendar.getInstance(); // Returns instance with current date and time set
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_TIME_STAMP);
        dataSnapshot.child(ORDERS_IN_PROGRESS).child(table_number + "").child(TIME_STAMP).getRef().setValue(formatter.format(calendar.getTime()));

        return false;
    }



    public static ArrayList<Dish> getOrderInProgress(int tableNum) {
        ArrayList<Dish> dishesArray = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(ORDERS_IN_PROGRESS).child(String.valueOf(tableNum)).child(DISHES).getChildren();
        for (DataSnapshot dish_snap : dish_iter) {
            System.out.println(dish_snap);
            Dish temp_dish = dish_snap.getValue(Dish.class);
            System.out.println(temp_dish.getName());
            dishesArray.add(temp_dish);
        }
        return dishesArray;

    }

    public static boolean deleteOrderFromProgress(int table_number) { // eden and ido
        firebaseReference.child(ORDERS_IN_PROGRESS).child("" + table_number).removeValue();
        return true;
    }


    public static boolean sendOrder(int table_number) { // eden and ido
        ArrayList<Dish> dishesArray =Database.getOrderInProgress(table_number);
        deleteOrderFromProgress(table_number);
        addOrderToLiveOrders(table_number,dishesArray);
        return true;
    }

    public static boolean endOrder(int table_number) { // eden and ido
        ArrayList<Dish> dishesArray =Database.getLiveOrder(table_number);
        deleteLiveOrder(table_number);
        deleteAskBill(table_number);

        addOrderToAskBill(table_number,dishesArray);
        return true;
    }

    private static void deleteAskBill(int table_number) {
        firebaseReference.child(AskBill).child("" + table_number).removeValue();

    }

    public static boolean addOrderToAskBill(int table_number, ArrayList<Dish> dishesArray) { // eden and ido
        int id=0;
        for (Dish dish: dishesArray){
            addDishToAskBill(table_number, dish, id);
            id++;
        }
        Calendar calendar = Calendar.getInstance(); // Returns instance with current date and time set
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_TIME_STAMP);
        dataSnapshot.child(AskBill).child(table_number + "").child(TIME_STAMP).getRef().setValue(formatter.format(calendar.getTime()));

        return true;
    }

    private static void addDishToAskBill(int table_number, Dish dish, int id) {
        firebaseReference.child(AskBill).child(table_number + "").child(DISHES).child(id+"").setValue(dish);

    }

    private static void deleteLiveOrder(int table_number) {
        firebaseReference.child(LIVE_ORDERS).child("" + table_number).removeValue();

    }

    public static boolean deleteDishFromOrderInProgress(int order_id, int dish_id) { // eden and ido
        firebaseReference.child(ORDERS_IN_PROGRESS).child("" + order_id).child(DISHES).child("" + dish_id).removeValue();
        return true;
    }

    public static void ChangeNotes(int dishPosition, String notes, int tableNum) { // eden and ido
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(ORDERS_IN_PROGRESS).child(String.valueOf(tableNum)).child(DISHES).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child(ORDERS_IN_PROGRESS).child(tableNum+"").child(DISHES).child(key).child(NOTES).getRef().setValue(notes);
                break;
            }
            counter++;
        }

    }

    public static boolean setPrice(String table_number, int dish_id, int new_price) {
        firebaseReference.child(LIVE_ORDERS).child(table_number).child(DISHES).child(""+dish_id).child(PRICE).setValue(new_price, completionListener);
        return true;
    }//manager

    public static boolean updateDishDetails(int dishPosition, Dish dish, String category) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).child(DISHES).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child(MENU).child(category).child(DISHES).child(key).getRef().setValue(dish, completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }

    public static boolean addDishToMenuByCategory(Dish dish, String category) { // we have here a serious problem
        // everything seeing to be ok and the addition works but when we refresh the menu it throw null pointer exception
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).child(DISHES).getChildren();
        long number_of_dishes = dataSnapshot.child(MENU).child(category).child(DISHES).getChildrenCount();
        int counter=0;
        String key="";
        for (DataSnapshot dish_snap : dish_iter) {
            if (counter == number_of_dishes - 1) {
                key = dish_snap.getKey();
            }
            counter++;
        }
        int newKey=Integer.parseInt(key)+1;
        firebaseReference.child(MENU).child(category).child(DISHES).child(newKey+"").setValue(dish, completionListener);
        return true;
    } //manager

    /**
     * this function delete dish from the database
     * @param dishPosition the positon of the dish to delete in the list
     * @param category the category of the dish
     * @return true if ok
     */
    public static boolean deleteDishFromMenu(int dishPosition, String category) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).child(DISHES).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                key = dish_snap.getKey();
                dataSnapshot.child(MENU).child(category).child(DISHES).child(key).getRef().removeValue(completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }//manger

    public static boolean deleteDishFromLiveOrders(int dishPosition, String table_number) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(LIVE_ORDERS).child(table_number).child(DISHES).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                key = dish_snap.getKey();
                dataSnapshot.child(LIVE_ORDERS).child(table_number).child(DISHES).child(key).getRef().removeValue(completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }//manger

    public static String getMenuImageURL(String menuName){
        //return dataSnapshot.child(MAIN_MENU_PICTURES).child(menuName).getValue(String.class);
        return dataSnapshot.child(MENU).child(menuName).child(IMAGE_URL).getValue(String.class);
    }

    public static void LoadImageFromWeb(final ImageView view, final Activity activity, @Nullable final HashMap<String, Drawable> imagesCash, final String url) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    final Drawable image = Drawable.createFromStream(is, SRC_NAME);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageDrawable(image);
                            if(imagesCash != null)
                                imagesCash.put(url, image);
                        }
                    });

                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(activity, "טעינת התמונה נכשלה", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
