package com.example.payeat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.payeat.Dish;
import com.example.payeat.Menu;
import com.example.payeat.Order;
import com.example.payeat.R;
import com.example.payeat.fragments.NamesFragment;

public class SplitBillActivity extends AppCompatActivity {

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);


        NamesFragment namesFragment = NamesFragment.newInstance();
        namesFragment.show(getSupportFragmentManager(), "Names Fragment");

        // Set the table number
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
        int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
        TextView tableNumTextView = findViewById(R.id.activity_split_bill_table_number_textView);
        tableNumTextView.setText("שולחן "+tableNum);

        // TODO get the real order (maybe with other dish object to represent the add ones)
        Dish[] dishes = {new Dish("Toast", 21.9, "Great toast"), new Dish("Lemonade", 12, "Cold lemonade")};
        order = new Order(dishes, 2);
    }
}