//
// Author: Ido Shapira
//
// This activity is shown right after the user login as a manager on the app.
// The manager can choose what he want to to:
// 1. Go to the menu restaurant
// 2. View and edit existing orders.
// 3. See the restaurant capacity.

package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.fragments.EditDishFromManagerFragment;
import com.example.payeat.interfaces.DataChangeListener;
import com.example.payeat.dataObjects.Database;
import com.example.payeat.R;

import java.util.HashMap;

public class ManagerOptionsActivity extends AppCompatActivity implements View.OnClickListener, DataChangeListener {

    TextView textViewManagerName;
    TextView textViewRestaurantName;
    ImageView imageViewRestaurantLogo;

    HashMap<String, Drawable> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        findViewById(R.id.button_list_of_existing_orders).setOnClickListener(this);
        findViewById(R.id.button_restaurant_occupancy).setOnClickListener(this);
        findViewById(R.id.button_view_menu).setOnClickListener(this);

        textViewManagerName = findViewById(R.id.textView_name_manager);
        textViewRestaurantName = findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = findViewById(R.id.imageView_restaurant_logo);

        images = new HashMap<>();

        String imageURL = Database.getRestaurantLogoURL();
        Drawable image = images.get(imageURL);
        if(image == null){ // not in cash
            Database.LoadImageFromWeb(imageViewRestaurantLogo, this, images, imageURL);
        } else {
            imageViewRestaurantLogo.setImageDrawable(image);
        }

        notifyOnChange();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_manager_name:
                return changeDetails("הכנס שם מנהל חדש:", "שם מנהל ריק!", Database.MANAGER_NAME);
            case R.id.edit_restaurant_name:
                return changeDetails("הכנס שם חדש למסעדה:", "שם מסעדה ריק!", Database.RESTAURANT_NAME);
            case R.id.edit_restaurant_logo:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean changeDetails(String title, String onErrorMassage, final String whatToUpdate) {
        final Dialog edit_manager_name_dialog = new Dialog(this);
        edit_manager_name_dialog.setContentView(R.layout.change_manager_details);
//        edit_manager_name_dialog.setTitle(title);
        edit_manager_name_dialog.setCancelable(true);

        TextView textView_title = edit_manager_name_dialog.findViewById(R.id.textView_title);
        textView_title.setText(title);

        final TextView textView_error = edit_manager_name_dialog.findViewById(R.id.textView_error);
        textView_error.setText(onErrorMassage);
        textView_error.setVisibility(View.GONE);

        final EditText editTextDetail = edit_manager_name_dialog.findViewById(R.id.editText_new_detail);
        Button OKbutton = edit_manager_name_dialog.findViewById(R.id.button_OK);
        Button Canclebutton = edit_manager_name_dialog.findViewById(R.id.button_cancel);

        OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_manager_name = editTextDetail.getText().toString();
                if(new_manager_name.compareTo("") == 0) {
                    textView_error.setVisibility(View.VISIBLE);
                }
                else {
                    Database.UpdateDetailsManager(whatToUpdate, new_manager_name);
                    edit_manager_name_dialog.dismiss();
                }
            }
        });

        Canclebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_manager_name_dialog.dismiss();
            }
        });
        edit_manager_name_dialog.show();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Database.addListener(this);
    }

    @Override
    protected void onPause() {
        Database.removeListener(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_view_menu:
                intent = new Intent(this, MainMenuActivity.class);
                intent.putExtra("mode manager", true);
                break;
            case R.id.button_list_of_existing_orders:
                intent = new Intent(this, ExistOrdersActivity.class);
                break;
            case R.id.button_restaurant_occupancy:
                intent = new Intent(this, RestaurantCapacityActivity.class);
                break;
            default:

        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void notifyOnChange() {
        textViewManagerName.setText("שלום, " + Database.getManagerName());
        textViewRestaurantName.setText("מסעדת: " + Database.getRestaurantName());
    }
}