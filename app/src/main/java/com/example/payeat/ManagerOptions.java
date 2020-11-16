package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ManagerOptions extends AppCompatActivity implements View.OnClickListener {

    TextView textViewManagerName;
    TextView textViewRestaurantName;
    ImageView imageViewRestaurantLogo;
    Button buttonToListViewOrdersActivity;

    Firebase firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        findViewById(R.id.button_list_of_existing_orders).setOnClickListener(this);
        findViewById(R.id.button_restaurant_occupancy).setOnClickListener(this); // need to think about this, how it should be?

        textViewManagerName = (TextView) findViewById(R.id.textView_name_manager);
        textViewRestaurantName = (TextView) findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = (ImageView) findViewById(R.id.imageView_restaurant_logo);
        buttonToListViewOrdersActivity = (Button) findViewById(R.id.button_list_of_existing_orders);

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
        switch (v.getId()) {
            case R.id.button_list_of_existing_orders:
                Intent intent = new Intent(this, ExistOrdersActivity.class);
                startActivity(intent);
                break;

            default:


        }
    }
}