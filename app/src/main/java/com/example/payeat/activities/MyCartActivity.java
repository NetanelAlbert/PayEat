package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.payeat.R;

public class MyCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
    }
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.order_after_viewing_cart_button :
                intent = new Intent(this, BonAppetitActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}
