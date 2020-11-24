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

import com.example.payeat.R;
import com.example.payeat.fragments.DishDetailsFragment;

import java.util.Arrays;
import java.util.List;

public class MyCartActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        MyCartActivity.DishAdapter adapter = new MyCartActivity.DishAdapter(this, R.layout.activity_my_cart_list_item,
                Arrays.asList("קרמבו", "קרמבו", "קרמבו", "קרמבו"));
        ListView DishListView = findViewById(R.id.category_menu_list);
        DishListView.setAdapter(adapter);
        DishListView.setOnItemClickListener(this);

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
    private class DishAdapter extends ArrayAdapter<String> {
        private List<String> list;
        public DishAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_my_cart_list_item, parent, false);
            }

            TextView title = convertView.findViewById(R.id.name_of_dish_text);
            title.setText(list.get(position));




            return convertView;
        }
    }

    @Override

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
