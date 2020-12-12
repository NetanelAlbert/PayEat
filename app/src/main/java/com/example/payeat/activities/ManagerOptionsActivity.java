//
// Author: Ido Shapira
//
// This activity is shown right after the user login as a manager on the app.
// The manager can choose what he want to to:
// 1. Go to the menu restaurant
// 2. View and edit existing orders.
// 3. See the restaurant capacity.

package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.payeat.interfaces.DataChangeListener;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.R;

import java.util.HashMap;

public class ManagerOptionsActivity extends AppCompatActivity implements View.OnClickListener, DataChangeListener {

    TextView textViewManagerName;
    TextView textViewRestaurantName;
    ImageView imageViewRestaurantLogo;

    HashMap<String, Drawable> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        findViewById(R.id.button_list_of_existing_orders).setOnClickListener(this);
        findViewById(R.id.button_restaurant_occupancy).setOnClickListener(this);
        findViewById(R.id.button_view_menu).setOnClickListener(this);

        textViewManagerName = findViewById(R.id.textView_name_manager);
        textViewRestaurantName = findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = findViewById(R.id.imageView_restaurant_logo);

        images = new HashMap<>();

        String imageURL = Database.getRestaurantLogoURL();
        Drawable image = images.get(imageURL);
        if(image == null){ // not in cash
            Database.LoadImageFromWeb(imageViewRestaurantLogo, this, images, imageURL);
        } else {
            imageViewRestaurantLogo.setImageDrawable(image);
        }

        notifyOnChange();

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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_view_menu:
                intent = new Intent(this, MainMenuActivity.class);
                intent.putExtra("mode manager", true);
                break;
            case R.id.button_list_of_existing_orders:
                intent = new Intent(this, ExistOrdersActivity.class);
                break;
            case R.id.button_restaurant_occupancy:
                intent = new Intent(this, RestaurantCapacityActivity.class);
                break;
            default:

        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void notifyOnChange() {
        textViewManagerName.setText("שלום, " + Database.getManagerName());
        textViewRestaurantName.setText("מסעדת: " + Database.getRestaurantName());
    }
}