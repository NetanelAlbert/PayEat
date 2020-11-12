package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerOptions extends AppCompatActivity {

    TextView textViewManagerName;
    TextView textViewRestaurantName;
    ImageView imageViewRestaurantLogo;

    Firebase firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        textViewManagerName = (TextView) findViewById(R.id.textView_name_manager);
        textViewRestaurantName = (TextView) findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = (ImageView) findViewById(R.id.imageView_restaurant_logo);

        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/");

        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String manager = dataSnapshot.child("manager_name").getValue(String.class);
                String restaurant = dataSnapshot.child("restaurant_name").getValue(String.class);

                textViewManagerName.setText(manager);
                textViewRestaurantName.setText(restaurant);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}