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

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_options);

        findViewById(R.id.button_list_of_existing_orders).setOnClickListener(this);
        findViewById(R.id.button_restaurant_occupancy).setOnClickListener(this);
        findViewById(R.id.button_view_menu).setOnClickListener(this);
        findViewById(R.id.fab_statistics).setOnClickListener(this);

        textViewManagerName = findViewById(R.id.textView_name_manager);
        textViewRestaurantName = findViewById(R.id.textView_restaurant_name);
        imageViewRestaurantLogo = findViewById(R.id.imageView_restaurant_logo);


        String imageURL = Database.getRestaurantLogoURL();
        Database.LoadImageFromWeb(imageViewRestaurantLogo, this, imageURL);

        notifyOnChange();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager_details, menu);
        return true;
    }

    private static final int REQUEST_CAMERA=1001;
    private static final int SELECT_FILE=1000;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_manager_name:
                return changeDetails("הכנס שם מנהל חדש:", "שם מנהל ריק!", Database.MANAGER_NAME);
            case R.id.edit_restaurant_name:
                return changeDetails("הכנס שם חדש למסעדה:", "שם מסעדה ריק!", Database.RESTAURANT_NAME);
            case R.id.edit_restaurant_logo:
                Toast.makeText(getApplicationContext(),"מצטערים! בגרסא הנוכחית אין אפשרות לערוך תמונה" , Toast.LENGTH_SHORT).show();
//                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                    //permission not granted for gallery, request it
//                    String[] permissions = {Manifest.permission.MANAGE_EXTERNAL_STORAGE};
//                    requestPermissions(permissions, REQUEST_CAMERA);
//                }
//                else if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//                    //permission not granted for camera, request it
//                    String[] permissions = {Manifest.permission.CAMERA};
//                    requestPermissions(permissions, SELECT_FILE);
//                }
//                else {
//                    //permission already granted
//                    selectImage();
//                }
//                return true;
//                // TODO update the database with the new image
        }
        return true;
    }

//    private void selectImage() {
//        final CharSequence[] items= {"מצלמה", "גלריה" , "בטל"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerOptionsActivity.this);
//        builder.setTitle("עדכן לוגו של המסעדה:");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(items[which].equals("מצלמה")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_CAMERA);
//                }
//                else if(items[which].equals("גלריה")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    startActivityForResult(intent.createChooser(intent, "בחר תמונה") , SELECT_FILE);
//                }
//                else if(items[which].equals("בטל")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    selectImage();
//                }
//                else {
//                    Toast.makeText(this, "הרשאה לקבצים לא אושרה!" , Toast.LENGTH_SHORT);
//                }
//                break;
//            case SELECT_FILE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    selectImage();
//                }
//                else {
//                    Toast.makeText(this, "הרשאה לקבצים לא אושרה!" , Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                selectImage();
//        }
//
//        selectImage();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if(requestCode == REQUEST_CAMERA) {
//                final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                imageViewRestaurantLogo.setImageBitmap(bitmap);
//            }
//            else if(requestCode == SELECT_FILE) {
//                Uri selectedImageUri = data.getData();
//                imageViewRestaurantLogo.setImageURI(selectedImageUri);
//            }
//            Toast.makeText(this, "Result ok", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Result canceled", Toast.LENGTH_LONG).show();
//        }
//    }


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
            case R.id.fab_statistics:
                intent = new Intent(this, StatisticsActivity.class);
                break;
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