package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean mode_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Set the list of menus
        MenusAdapter adapter = new MenusAdapter(this, R.layout.activity_main_menu_gridview,
                Arrays.asList("ארוחת בוקר","משקאות קלים","קינוחים","אלכוהול"));
        GridView menusGridView = findViewById(R.id.activity_main_menu_gridview);
        menusGridView.setAdapter(adapter);
        menusGridView.setOnItemClickListener(this);

        TextView tableNumTextView = findViewById(R.id.activity_main_menu_table_number_textView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        mode_manager = getIntent().getBooleanExtra("mode manager", false);
        if(mode_manager) {
            tableNumTextView.setVisibility(View.GONE);
        }
        else {
            View navigationLayout = findViewById(R.id.main_menu_navigation_layout);
            navigationLayout.setVisibility(View.GONE);
            // Set the table number
            SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
            int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
            tableNumTextView.setText(String.format(getString(R.string.table_number_format), tableNum));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "onItemClick - " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MenuByTitleActivity.class);
        intent.putExtra("mode manager", mode_manager);
        intent.putExtra(getResources().getString(R.string.intent_extras_menu_id),i); // TODO change 'i' to the real id according to database.
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                return true;
            case R.id.orders:
                startActivity(new Intent(getApplicationContext(), ExistOrdersActivity.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            case R.id.restaurant_capacity:
                startActivity(new Intent(getApplicationContext(), RestaurantOccupancyActivity.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

    private class MenusAdapter extends ArrayAdapter<String>{
        public MenusAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_menu_gridview, parent, false);
            }
            TextView title = convertView.findViewById(R.id.main_menu_item_textView);
            title.setText(getItem(position));

            return convertView;
        }
    }

}