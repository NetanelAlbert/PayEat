package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payeat.dataObjects.Database;
import com.example.payeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private boolean mode_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Set the list of menus
        MenusAdapter adapter = new MenusAdapter(this, this, R.layout.activity_main_menu_gridview,
                Database.getCategories());
        GridView menusGridView = findViewById(R.id.activity_main_menu_gridview);
        menusGridView.setAdapter(adapter);
        menusGridView.setOnItemClickListener(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Button goToCartButton = findViewById(R.id.activity_main_menu_table_number_textView_go_to_my_cart_button);
        goToCartButton.setOnClickListener(this);

        TextView tableNumTextView = findViewById(R.id.activity_main_menu_table_number_textView);
        mode_manager = getIntent().getBooleanExtra("mode manager", false);
        if(mode_manager) {
            tableNumTextView.setVisibility(View.GONE);
            goToCartButton.setVisibility(View.GONE);
        }
        else {
            View navigationLayout = findViewById(R.id.main_menu_navigation_layout);
            navigationLayout.setVisibility(View.GONE);
            // Set the table number
            SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
            int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
            tableNumTextView.setText(String.format(getString(R.string.table_number_format), tableNum));


        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_main_menu_table_number_textView_go_to_my_cart_button){
            Intent intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, MenuByTitleActivity.class);
        intent.putExtra("mode manager", mode_manager);
        intent.putExtra(getResources().getString(R.string.intent_extras_menu_name),(String) adapterView.getItemAtPosition(i));
        startActivity(intent);
    }

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
                startActivity(new Intent(getApplicationContext(), RestaurantCapacityActivity.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

    private class MenusAdapter extends ArrayAdapter<String>{
        private final Activity activityParent;
        private final HashMap<String, Drawable> images;

        public MenusAdapter(@NonNull Context context, Activity activity, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.activityParent = activity;
            images = new HashMap<>();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_menu_gridview, parent, false);
            }
            TextView title = convertView.findViewById(R.id.main_menu_item_textView);
            String menuName = getItem(position);
            title.setText(menuName);

            ImageView imageView = convertView.findViewById(R.id.main_menu_item_imageView);
            String menuImageURL = Database.getMenuImageURL(getItem(position));
            Drawable image = images.get(menuImageURL);
            if(image == null){ // not in cash
                Database.LoadImageFromWeb(imageView, activityParent, images, menuImageURL);
            } else {
                imageView.setImageDrawable(image);
            }

            return convertView;
        }
        @Override

        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }
    }

}