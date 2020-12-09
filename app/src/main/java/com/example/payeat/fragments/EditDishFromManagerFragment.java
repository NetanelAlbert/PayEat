package com.example.payeat.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.payeat.Database;
import com.example.payeat.Dish;
import com.example.payeat.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDishFromManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDishFromManagerFragment extends DialogFragment implements View.OnClickListener {

    private EditText editText_new_name;
    private EditText editText_new_price;
    private EditText editText_new_desc;
    /**
     * @param dish_name the dish name.
     * @param dish_price the dish price.
     * @param dish_desc the dish description.
     */
    private String dish_name;
    private int dish_price;
    private String dish_desc;

    private ImageView dish_image;
    private boolean newDish;

    public EditDishFromManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EditDishFromManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDishFromManagerFragment newInstance() {
        EditDishFromManagerFragment fragment = new EditDishFromManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        dish_image = getActivity().findViewById(R.id.dish_image);
//        Drawable image = dish_image.getDrawable();
//        dish_image = getView().findViewById(R.id.ImageView_update_dish_image);
//        dish_image.setImageDrawable(image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_edit_dish_from_manager, container, false);

        Button sendButton = convertView.findViewById(R.id.button_update_dish);
        sendButton.setOnClickListener(this);

        Button cancelCostButton = convertView.findViewById(R.id.button_cancel_update_dish);
        cancelCostButton.setOnClickListener(this);

        editText_new_name = convertView.findViewById(R.id.editText_update_dish_name);
        editText_new_price = convertView.findViewById(R.id.editText_update_dish_cost);
        editText_new_desc = convertView.findViewById(R.id.editText_update_dish_description);

        // get param from the bundle
        dish_name = getArguments().getString("name");
        dish_desc = getArguments().getString("desc");
        dish_price = getArguments().getInt("price");

        newDish = false;
        if(dish_name == null) {
            newDish = true;
            sendButton.setText("הוסף");
        }

        editText_new_name.setText(dish_name);
        editText_new_price.setText(String.valueOf(dish_price));
        editText_new_desc.setText(dish_desc);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_update_dish:

                dish_name = ""+editText_new_name.getText();
                dish_price = Integer.parseInt(""+editText_new_price.getText());
                dish_desc = ""+editText_new_desc.getText();

                Dish new_dish = new Dish(dish_name, dish_price, dish_desc, true, 0, "");
                String category = getArguments().getString("category");

                if(newDish) {
                    Database.addDishToMenuByCategory(new_dish, category);
                }
                else {
                    int position = getArguments().getInt("dish_position");
                    Database.updateDishDetails(position, new_dish, category);
                }
                dismiss();
                break;
            case R.id.button_cancel_update_dish:
                dismiss();
                break;
        }
    }
}