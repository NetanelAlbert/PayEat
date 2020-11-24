package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.payeat.R;

public class BonAppetitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_appetit);
    }
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.go_back_to_menu_button :
                intent = new Intent(this, MainMenuActivity.class);
                break;
            case R.id.view_check_button:
                intent = new Intent(this, SplitBillActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}
