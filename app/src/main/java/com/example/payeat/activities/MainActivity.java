package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.payeat.dataObjects.Database;
import com.example.payeat.fragments.ChooseTableFragment;
import com.example.payeat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v.getId() == R.id.activity_main_customer_button){
                fragment = ChooseTableFragment.newInstance(this);
                fragment.show(getSupportFragmentManager(), "ChooseTableFragment");

        } else if(v.getId() == R.id.activity_main_manager_button){
                intent = new Intent(this, ManagerLoginActivity.class);

        } else if(v.getId() ==  R.id.fragment_choose_table_Button){
                String tableNumberS = fragment.getTableNumber();
                if(tableNumberS == null || tableNumberS.length() == 0)
                    return;
                int tableNumber = Integer.parseInt(tableNumberS);
                if(tableNumber > Database.getMaxTableNumber()){
                    Toast.makeText(this, "מספר שולחן לא יכול להיות גבוה מ-"+Database.getMaxTableNumber(), Toast.LENGTH_SHORT).show();
                    return;
                }


                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putInt(getString(R.string.client_table_number), tableNumber);
                prefEditor.apply();
                fragment.dismiss();
                intent = new Intent(this, MainMenuActivity.class);

        }
        if(intent != null)
            startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}