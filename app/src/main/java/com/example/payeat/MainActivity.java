package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_customer_button).setOnClickListener(this);
        findViewById(R.id.activity_main_manager_button).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_customer_button :

                break;
            case R.id.activity_main_manager_button :


                break;

            default:


        }
    }
}