package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ManagerOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewManagerName;
    TextView textViewRestaurantName;
    ImageView imageViewRestaurantLogo;

    Firebase firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        findViewById(R.id.button_list_of_existing_orders).setOnClickListener(this);
        findViewById(R.id.button_restaurant_occupancy).setOnClickListener(this); // need to think about this, how it should be?
        findViewById(R.id.button_view_menu).setOnClickListener(this);

        textViewManagerName = (TextView) findViewById(R.id.textView_name_manager);
        textViewRestaurantName = (TextView) findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = (ImageView) findViewById(R.id.imageView_restaurant_logo);

        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");

        firebaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String manager = dataSnapshot.child("manager_name").getValue(String.class);
                String restaurant = dataSnapshot.child("restaurant_name").getValue(String.class);

                textViewManagerName.setText("שלום, " + manager);
                textViewRestaurantName.setText("מסעדת: " + restaurant);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_view_menu:
                intent = new Intent(this, ManagerMenuActivity.class);
                startActivity(intent);
            case R.id.button_list_of_existing_orders:
                intent = new Intent(this, ExistOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.button_restaurant_occupancy:
                intent = new Intent(this, RestaurantOccupancyActivity.class);
                startActivity(intent);
            default:


        }
    }
}