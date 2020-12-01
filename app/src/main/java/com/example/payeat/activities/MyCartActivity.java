package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.Dish;
import com.example.payeat.R;
import com.example.payeat.fragments.DishDetailsFragment;

import java.util.Arrays;
import java.util.List;

public class MyCartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,View.OnClickListener {
    private Button orderYourOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
       // findViewById(R.id.go_to_my_cart_button).setOnClickListener(this);
        DishAdapter adapter = new MyCartActivity.DishAdapter(this, R.layout.activity_my_cart_list_item,
                Arrays.asList(new Dish( "food name", 100, "very tasty"), new Dish( "omlet", 10, "very hot")));
        ListView DishListView = findViewById(R.id.my_cart_list);
        DishListView.setAdapter(adapter);
        DishListView.setOnItemClickListener(this);
        orderYourOrder = (Button) findViewById(R.id.order_after_viewing_cart_button);
        orderYourOrder.setOnClickListener((View.OnClickListener) this);


        //  mode_manager = getIntent().getBooleanExtra("mode manager", false);
      //  goToCart = (Button) findViewById(R.id.go_to_my_cart_button);

        //Setting listeners to button
      //  goToCart.setOnClickListener((View.OnClickListener) this);

        //if(mode_manager) {
          //  goToCart.setVisibility(View.GONE);
        //}
    }
//    @Override
//    public void onClick(View v) {
//        if(v.getId()==R.id.go_to_my_cart_button)
//            Toast.makeText(this, "הולך לעגלה!", Toast.LENGTH_SHORT ).show();
//
//
//    }
public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

}
    private class DishAdapter extends ArrayAdapter<Dish> {
        public DishAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

         if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_my_cart_list_item, parent, false);
        }

            TextView dishName = convertView.findViewById(R.id.name_of_dish_text);
            dishName.setText(getItem(position).getName());

            TextView description = convertView.findViewById(R.id.detailes_of_dish_text);
            //TODO chang to information about this specific order dish (i.e. the chosen topics on a pizza)
            description.setText(getItem(position).getDesc());

            TextView price = convertView.findViewById(R.id.price_of_dish_text);
            price.setText(String.valueOf(getItem(position).getPrice()));

        Button cancelDishButton =  convertView.findViewById(R.id.cancel_dish_button);
                cancelDishButton.setVisibility(View.VISIBLE);
                cancelDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ביטלתי!", Toast.LENGTH_SHORT).show();
            }
        });

            return convertView;
    }
    }

    @Override

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.order_after_viewing_cart_button :
                Toast.makeText(this, "מייד מגיע!", Toast.LENGTH_SHORT).show();

                intent = new Intent(this, BonAppetitActivity.class);
                break;
            default:


        }
        if(intent != null)
            startActivity(intent);
    }
}
