package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewDisplay;
    //Firebase firebaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_customer_button).setOnClickListener(this);
        findViewById(R.id.activity_main_manager_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

//        textViewDisplay = (TextView) findViewById(R.id.textView_from_database);
//        firebaseReference = new Firebase("https://payeat-4a103.firebaseio.com/test");
//
//        firebaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                textViewDisplay.setText(text);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_customer_button :
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_manager_button :
                Intent intent2 = new Intent(this, ManagerOptionsActivity.class);
                startActivity(intent2);
                break;

            default:


        }
    }
}