package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.payeat.R;

public class BonAppetitActivity extends AppCompatActivity implements View.OnClickListener{

    private Button backToMenu;
    private Button viewCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_appetit);
        backToMenu = (Button) findViewById(R.id.go_back_to_menu_button);
        backToMenu.setOnClickListener((View.OnClickListener) this);
        viewCheck = (Button) findViewById(R.id.view_check_button);
        viewCheck.setOnClickListener((View.OnClickListener) this);
    }
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.go_back_to_menu_button :
                Toast.makeText(this, "חוזר לתפריט הראשי!", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainMenuActivity.class);
                break;
            case R.id.view_check_button:
                Toast.makeText(this, "הולך לחשבון!", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, SplitBillActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}
