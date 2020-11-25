package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.payeat.fragments.ChooseTableFragment;
import com.example.payeat.R;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    Firebase firebaseReference;
    private ChooseTableFragment fragment;
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
        Intent intent = null;
        switch (v.getId()){
            case R.id.activity_main_customer_button :
                fragment = ChooseTableFragment.newInstance(this);
                fragment.show(getSupportFragmentManager(), "ChooseTableFragment");
                break;
            case R.id.activity_main_manager_button :
                intent = new Intent(this, ManagerLoginActivity.class);
                break;
            case R.id.fragment_choose_table_Button :
                String tableNumberS = fragment.getTableNumber();
                if(tableNumberS == null || tableNumberS.length() == 0)
                    return;
                int tableNumber = Integer.parseInt(tableNumberS);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putInt(getString(R.string.client_table_number), tableNumber);
                prefEditor.apply();
                fragment.dismiss();
                intent = new Intent(this, MainMenuActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}