package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RestaurantOccupancyActivity extends AppCompatActivity {

    ListView listView_capacity;

    String[] table_number_list = {"שולחן 1", "שולחן 2", "שולחן 3", "שולחן 4", "שולחן 5"}; // take from database
    String[] is_occupied_list = {"yes", "no", "yes", "yes", "no"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_occupancy);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.restaurant_capacity);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu:
                        startActivity(new Intent(getApplicationContext(), ManagerMenuActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), ExistOrdersActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.restaurant_capacity:
                        return true;
                }
                return false;
            }
        });

        listView_capacity = findViewById(R.id.listView_capacity);


//        TableRow tableRow;
//        ImageView imageView;
//        TableLayout tableLayout = (TableLayout) findViewById(R.id.TableLayout_capacity);
//
//        for (int i = 0; i < 2; i++) {
//            tableRow = new TableRow(this);
//            for (int j = 0; j < 2; j++) {
//
//                imageView = new ImageView(this);
//                ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Or a custom size
//                params.width = ViewGroup.LayoutParams.WRAP_CONTENT; // Or a custom size
//                imageView.setLayoutParams(params);
//                imageView.setImageResource(R.drawable.capacity_icon);
//                tableRow.addView(imageView);
//            }
//            tableLayout.addView(tableRow);
//        }

        CapacityListAdapter capacityListAdapter = new CapacityListAdapter(this, table_number_list, is_occupied_list);
        listView_capacity.setAdapter(capacityListAdapter);
        listView_capacity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(RestaurantOccupancyActivity.this, "clice", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class CapacityListAdapter extends ArrayAdapter<String> {

        Context context;
        String rtable_number_list[];
        String ris_occupied_list[];

        CapacityListAdapter (Context c, String[] title, String[] description) {
            super(c, R.layout.capacity_row, R.id.title_order, title);
            this.context = c;
            this.rtable_number_list = title;
            this.ris_occupied_list = description;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.capacity_row, parent, false);

            TextView myTitle = row.findViewById(R.id.textView_table_id);
            myTitle.setText(rtable_number_list[position]);

            TextView is_occupied = row.findViewById(R.id.textView_occupied);
            is_occupied.setText(ris_occupied_list[position]);

            return row;
        }
    }
}