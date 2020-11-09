package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;import android.widget.ListView;

public class MenuByTitleActivity extends AppCompatActivity implements View.OnClickListener {
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_by_title);
        findViewById(R.id.expand_dish_button).setOnClickListener(this);
        findViewById(R.id.order_dish_button).setOnClickListener(this);
        setContentView(R.layout.activity_main);
        simpleList = (ListView)findViewById(R.id.category_menu_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_menu_by_title, R.id.textView, countryList);
        simpleList.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.expand_dish_button:
                break;
            case R.id.order_dish_button:
                break;


        }
    }
}