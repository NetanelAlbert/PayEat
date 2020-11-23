package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.payeat.R;
import com.example.payeat.fragments.ChooseTableFragment;

public class ManagerLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
    }
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.activity_manager_login_button :
                intent = new Intent(this, ManagerOptionsActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}