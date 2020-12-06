package com.example.payeat;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Database extends android.app.Application implements ValueEventListener {
    private static Firebase firebaseReference;
    private static DataSnapshot dataSnapshot;
    private static ArrayList<DataChangeListener> listeners;
    public static final String dishes="dishes";
    public static final String liveOrders="live_orders";
    public static final String ordersInProgress="orders_in_progress";
    public static final String mainMenuPictures="main_menu_pictures";
    public static final String managerName="manager_name";
    public static final String restaurantName="restaurant_name";
    public static final String Password="password";
    public static final String maxTableNumber="max_table_number";
    public static final String timeStamp="time-stamp";
    public static final String Price="price";
    public static final String Notes="notes";
    public static final String formatTimeStamp="dd/MM/yyyy - HH:mm:ss";
    public static final String srcName="src name";






    private static final String MENU="menu";


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
        return dataSnapshot.child(managerName).getValue(String.class);
    }

    public static String getRestaurantName() {
        return dataSnapshot.child(restaurantName).getValue(String.class);
    }

    public static int getMaxTableNumber() {
        return dataSnapshot.child(maxTableNumber).getValue(Integer.class);
    }
    public static String getPassword() {
        return dataSnapshot.child(Password).getValue(String.class);
    }


    public static ArrayList<Order> getOrders(String fromWhere) { // ido
        ArrayList<Order> result = new ArrayList<>();
        Iterable<DataSnapshot> order_iter = dataSnapshot.child(fromWhere).getChildren();
        for (DataSnapshot order_snap: order_iter) {
            ArrayList<Dish> dishesOfOrder = new ArrayList<>();
            Iterable<DataSnapshot> dish_iter = order_snap.child(dishes).getChildren();
            for (DataSnapshot dish_snap: dish_iter) {
                Dish temp_dish = dish_snap.getValue(Dish.class);
                dishesOfOrder.add(temp_dish);
            }
            int table_number = Integer.parseInt(order_snap.getKey());
            String timeFromDatabase = order_snap.child(timeStamp).getValue(String.class);
            Calendar timestamp = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(formatTimeStamp);
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
        System.out.println("id= "+id);
        Iterable<DataSnapshot> categories_iter = dataSnapshot.child(MENU).getChildren();
        int i=0;
        for (DataSnapshot category_snap: categories_iter) {
            System.out.println(i+"    "+category_snap.getKey());
            if(i!=id)
                i++;
            else
                return category_snap.getKey();
        }
        return "error";
    }

    public static Dish getDishFromMenu(String category, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child(MENU).child(category).child(dish_id+"");
        return d.getValue(Dish.class);
    }

    public static Dish getDishFromOrders(int order_id, int dish_id) { // edut and ido
        DataSnapshot d= dataSnapshot.child(liveOrders).child(order_id+"").child(dishes).child(dish_id+"");
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
        firebaseReference.child(liveOrders).child(table_number + "").child(dishes).child(id+"").setValue(dish);
        return true;

    }


    public static boolean addOrderToLiveOrders(int table_number, ArrayList<Dish> dishesArray) { // eden and ido
        int id;
        if(dataSnapshot.child(liveOrders).child(""+ table_number).child(dishes).getChildrenCount()==0)
            id=0;
        else
            id=getMaxId(table_number);
        for (Dish dish: dishesArray){
            addDishToLiveOrder(table_number, dish, id);
            id++;
        }
        Calendar calendar = Calendar.getInstance(); // Returns instance with current date and time set
        SimpleDateFormat formatter = new SimpleDateFormat(formatTimeStamp);
        dataSnapshot.child(liveOrders).child(table_number + "").child(timeStamp).getRef().setValue(formatter.format(calendar.getTime()));

        return true;
    }

    private static int getMaxId(int table_number) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(liveOrders).child(table_number + "").child(dishes).getChildren();
        long numOfDishes = dataSnapshot.child(liveOrders).child(table_number + "").child(dishes).getChildrenCount();
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
    public static Menu getMenuByCategory(String category) { // edut
        ArrayList<Dish> dishesArray = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).getChildren();
        for (DataSnapshot dish_snap: dish_iter) {
            Dish temp_dish = dish_snap.getValue(Dish.class);
            dishesArray.add(temp_dish);
        }
        return new Menu(category,dishesArray);
    }


    public static boolean addDishToOrderInProgress(int table_number, Dish dish) {
        long numOfDishesInOrder = dataSnapshot.child(ordersInProgress).child(String.valueOf(table_number)).child(dishes).getChildrenCount();
        firebaseReference.child(ordersInProgress).child(String.valueOf(table_number)).child(dishes).child(String.valueOf(numOfDishesInOrder)).setValue(dish);
        return false;
    }



    public static ArrayList<Dish> getOrderInProgress(int tableNum) {
        ArrayList<Dish> dishesArray = new ArrayList<>();
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(ordersInProgress).child(String.valueOf(tableNum)).child(dishes).getChildren();
        for (DataSnapshot dish_snap : dish_iter) {
            System.out.println(dish_snap);
            Dish temp_dish = dish_snap.getValue(Dish.class);
            System.out.println(temp_dish.getName());
            dishesArray.add(temp_dish);
        }
        return dishesArray;

    }

    public static boolean deleteOrderFromProgress(int table_number) { // eden and ido
        firebaseReference.child(ordersInProgress).child("" + table_number).removeValue();
        return true;
    }


    public static boolean sendOrder(int table_number) { // eden and ido
        ArrayList<Dish> dishesArray =Database.getOrderInProgress(table_number);
        deleteOrderFromProgress(table_number);
        addOrderToLiveOrders(table_number,dishesArray);
        return true;
    }

    public static boolean deleteDishFromOrderInProgress(int order_id, int dish_id) { // eden and ido
        firebaseReference.child(ordersInProgress).child("" + order_id).child(dishes).child("" + dish_id).removeValue();
        return true;
    }

    public static boolean ChangeNotes(int dishPosition, String notes, int tableNum) { // eden and ido
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(ordersInProgress).child(String.valueOf(tableNum)).child(dishes).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child(ordersInProgress).child(tableNum+"").child(dishes).child(key).child(Notes).getRef().setValue(notes);
                break;
            }
            counter++;
        }
        
        return true;
    }

    public static boolean setPrice(String table_number, int dish_id, double new_price) {
        firebaseReference.child(liveOrders).child(table_number).child(dishes).child(""+dish_id).child(Price).setValue(new_price, completionListener);
        return true;
    }//manager

    public static boolean updateDishDetails(int dishPosition, Dish dish, String category) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                System.out.println(dish_snap);
                key = dish_snap.getKey();
                dataSnapshot.child(MENU).child(category).child(key).getRef().setValue(dish, completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }

    public static boolean addDishToMenuByCategory(Dish dish, String category) { // we have here a serious problem
        // everything seeing to be ok and the addition works but when we refresh the menu it throw null pointer exception
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).getChildren();
        long number_of_dishes = dataSnapshot.child(MENU).child(category).getChildrenCount();
        int counter=0;
        String key="";
        for (DataSnapshot dish_snap : dish_iter) {
            if (counter == number_of_dishes - 1) {
                key = dish_snap.getKey();
            }
            counter++;
        }
        int newKey=Integer.parseInt(key)+1;
        firebaseReference.child(MENU).child(category).child(newKey+"").setValue(dish, completionListener);
        return true;
    } //manager

    /**
     * this function delete dish from the database
     * @param dishPosition the positon of the dish to delete in the list
     * @param category the category of the dish
     * @return true if ok
     */
    public static boolean deleteDishFromMenu(int dishPosition, String category) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(MENU).child(category).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                key = dish_snap.getKey();
                dataSnapshot.child(MENU).child(category).child(key).getRef().removeValue(completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }//manger

    public static boolean deleteDishFromLiveOrders(int dishPosition, String table_number) {
        Iterable<DataSnapshot> dish_iter = dataSnapshot.child(liveOrders).child(table_number).child(dishes).getChildren();
        int counter=0;
        String key;
        for (DataSnapshot dish_snap : dish_iter) {
            if(counter==dishPosition) {
                key = dish_snap.getKey();
                dataSnapshot.child(liveOrders).child(table_number).child(dishes).child(key).getRef().removeValue(completionListener);
                return true;
            }
            counter++;
        }
        return false;
    }//manger

    public static String getMenuImageURL(String menuName){
        return dataSnapshot.child(mainMenuPictures).child(menuName).getValue(String.class);
    }

    public static void LoadImageFromWeb(final ImageView view, final Activity activity, final String url) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    final Drawable image = Drawable.createFromStream(is, srcName);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageDrawable(image);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(activity, "טעינת התמונה נכשלה", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
