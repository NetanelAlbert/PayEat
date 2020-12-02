
package com.example.payeat.activities;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
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

        import com.example.payeat.Database;
        import com.example.payeat.Dish;
        import com.example.payeat.fragments.DishDetailsFragment;
        import com.example.payeat.R;
        import com.example.payeat.fragments.OrderDishFragment;
        import com.google.android.material.bottomnavigation.BottomNavigationView;

        import java.util.Arrays;
        import java.util.List;

public class MenuByTitleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,View.OnClickListener {
    private DishDetailsFragment fragment1;
    private boolean mode_manager;
    private Button goToCart;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int categoryId=getIntent().getIntExtra("menu id", 0);
        setContentView(R.layout.activity_menu_by_title);
        DishAdapter adapter = new DishAdapter(this, R.layout.activity_menu_by_title_list_item,
                (Database.getMenuByCategory(Database.getCategoryNameByNumber(categoryId)).getDishes()));

        TextView category = findViewById(R.id.category_name_text);
        category.setText(Database.getCategoryNameByNumber(categoryId));

        //TextView table = findViewById(R.id.table_number_in_menu);
        //String tableNum=getIntent().getStringExtra("tableNum", 0);
       // category.setText("מספר שולחן: ");

        ListView DishListView = findViewById(R.id.category_menu_list);
        DishListView.setAdapter(adapter);
        DishListView.setOnItemClickListener(this);
        mode_manager = getIntent().getBooleanExtra("mode manager", false);
        goToCart = (Button) findViewById(R.id.go_to_my_cart_button);

        //Setting listeners to button
        goToCart.setOnClickListener((View.OnClickListener) this);

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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.go_to_my_cart_button) {
            Toast.makeText(this, "הולך לעגלה!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        }

    }

    private class DishAdapter extends ArrayAdapter<Dish>{
        public DishAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_menu_by_title_list_item, parent, false);
            }
            final String name=getItem(position).getName();
            final String desc=getItem(position).getDesc();
            final String price =getItem(position).getPrice()+"";
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


                fragment1 = DishDetailsFragment.newInstance(mode_manager);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("desc", desc);
                        bundle.putString("price", price+"");
                        fragment1.setArguments(bundle);

                        System.out.println("\n\n\nname from bundle="+name);
                fragment1.show(getSupportFragmentManager(), "DishDetailsFragment");
                    }
                });

            return convertView;
        }
    }

}