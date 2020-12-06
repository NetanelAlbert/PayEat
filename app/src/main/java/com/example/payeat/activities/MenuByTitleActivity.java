
package com.example.payeat.activities;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;

        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.payeat.DataChangeListener;
        import com.example.payeat.Database;
        import com.example.payeat.Dish;
        import com.example.payeat.fragments.DeleteDishFragment;
        import com.example.payeat.fragments.DishDetailsFragment;
        import com.example.payeat.R;
        import com.example.payeat.fragments.OrderDishFragment;
        import com.google.android.material.bottomnavigation.BottomNavigationView;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class MenuByTitleActivity extends AppCompatActivity implements View.OnClickListener, DataChangeListener {
    private DishDetailsFragment dishDetailsFragment;
    private DeleteDishFragment deleteDishFragment;
    private boolean mode_manager;
    private Button goToCart;
    private String String_category;
    private int categoryId;
    private int tableNum;
    private DishAdapter adapter;
    ListView DishListView;

    private String name;
    private String desc;
    private double price;
    private boolean in_stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId=getIntent().getIntExtra("menu id", 0);
        setContentView(R.layout.activity_menu_by_title);
        TextView category = findViewById(R.id.category_name_text);
        String_category=Database.getCategoryNameByNumber(categoryId);
        category.setText(String_category);
        TextView tableNumTextView = findViewById(R.id.table_number_in_menu);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
        tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
        tableNumTextView.setText("שולחן "+tableNum);
        DishListView = findViewById(R.id.category_menu_list);
        mode_manager = getIntent().getBooleanExtra("mode manager", false);
        goToCart = (Button) findViewById(R.id.go_to_my_cart_button);
        //Setting listeners to button
        goToCart.setOnClickListener(this);
        if(mode_manager) {
            tableNumTextView.setVisibility(View.GONE);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu:
                        return true;
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), ExistOrdersActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.restaurant_capacity:
                        startActivity(new Intent(getApplicationContext(), RestaurantOccupancyActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        if(mode_manager) {
            goToCart.setVisibility(View.GONE);
        }
        else {
            bottomNavigationView.setVisibility(View.GONE);
        }
        notifyOnChange();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.go_to_my_cart_button) {
            Toast.makeText(this, "הולך לעגלה!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }

    @Override
    public void notifyOnChange() {
        ArrayList<Dish> dishes = (ArrayList<Dish>) Database.getMenuByCategory(Database.getCategoryNameByNumber(categoryId)).getDishes();
        adapter = new DishAdapter(this, R.layout.activity_menu_by_title_list_item,dishes);
        DishListView.setAdapter(adapter);
    }

    private class DishAdapter extends ArrayAdapter<Dish>{
        public DishAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_menu_by_title_list_item, parent, false);
            }
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    System.out.println("view.getId():-->" + v.getId());
                    if(mode_manager) {
                        deleteDishFragment = DeleteDishFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("category", String_category);
                        bundle.putInt("dish_position", position);
                        bundle.putString("deleteFrom", "menu");
                        deleteDishFragment.setArguments(bundle);
                        deleteDishFragment.show(getSupportFragmentManager(), "DeleteDishFragment");
                    }
                    return true;
                }
            });

            name=getItem(position).getName();
            desc=getItem(position).getDescription();
            price =getItem(position).getPrice();
            in_stock =getItem(position).isIn_stock();

            TextView dishName = convertView.findViewById(R.id.dish_name_text);
            dishName.setText(name);

            TextView description = convertView.findViewById(R.id.dish_detailes_text);
            //TODO chang to information about this specific order dish (i.e. the chosen topics on a pizza)
            description.setText(desc);

            TextView Dishprice = convertView.findViewById(R.id.dish_price_text);
            Dishprice.setText(String.valueOf(price));
            Button expandDishButton =  convertView.findViewById(R.id.expand_dish_button);
            expandDishButton.setVisibility(View.VISIBLE);
            expandDishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                dishDetailsFragment = DishDetailsFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("category", String_category);
                bundle.putString("name", name);
                bundle.putString("desc", desc);
                bundle.putDouble("price", price);
                bundle.putBoolean("in_stock", in_stock);
                bundle.putBoolean("mode_manager", mode_manager);
                if(mode_manager) {
                    bundle.putInt("tableNum", position);
                }
                else {
                    bundle.putInt("tableNum", tableNum);
                }
                dishDetailsFragment.setArguments(bundle);

                System.out.println("\n\n\nname from bundle="+name);
                dishDetailsFragment.show(getSupportFragmentManager(), "DishDetailsFragment");
                    }
                });



            return convertView;
        }
    }

}