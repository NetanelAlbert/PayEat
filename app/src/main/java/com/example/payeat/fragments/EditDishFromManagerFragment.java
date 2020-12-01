package com.example.payeat.fragments;

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

    private static String DISH_NAME;
    private static String DISH_PRICE;
    private static String DISH_DESC;
    private static String DISH_IMAGE;

    private EditText editText_new_name;
    private EditText editText_new_price;
    private EditText editText_new_desc;

    private String dish_name;
    private int dish_price;
    private String dish_desc;

    private ImageView dish_image;

    Firebase firebaseReference;


    public EditDishFromManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Pdish_name the dish name.
     * @param Pdish_price the dish price.
     * @param Pdish_desc the dish description.
     * @return A new instance of fragment EditDishFromManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDishFromManagerFragment newInstance(String Pdish_name, int Pdish_price, String Pdish_desc) {
        EditDishFromManagerFragment fragment = new EditDishFromManagerFragment();
        Bundle args = new Bundle();
        args.putString(DISH_NAME, Pdish_name);
        args.putInt(DISH_PRICE, Pdish_price);
        args.putString(DISH_DESC, Pdish_desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dish_name = getArguments().getString(DISH_NAME);
            dish_price = getArguments().getInt(DISH_PRICE);
            dish_desc = getArguments().getString(DISH_DESC);
        }
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

        Button updateCostButton = convertView.findViewById(R.id.button_update_dish);
        updateCostButton.setOnClickListener(this);

        Button cancelCostButton = convertView.findViewById(R.id.button_cancel_update_dish);
        cancelCostButton.setOnClickListener(this);

        editText_new_name = convertView.findViewById(R.id.editText_update_dish_name);
        editText_new_price = convertView.findViewById(R.id.editText_update_dish_cost);
        editText_new_desc = convertView.findViewById(R.id.editText_update_dish_description);

        editText_new_name.setText(dish_name);
        editText_new_price.setText(""+dish_price);
        editText_new_desc.setText(dish_desc);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_update_dish:

                firebaseReference = Database.getDataBaseInstance();

                dish_name = ""+editText_new_name.getText();
                dish_price = Integer.parseInt(""+editText_new_price.getText());
                dish_desc = ""+editText_new_desc.getText();
                // send name,price,desc to the database

//                Dish dish = new Dish(dish_name, dish_price, dish_desc);
//                dish.id = firebaseReference.push().getKey();
//
//                DatabaseReference.CompletionListener completionListener = new
//                        DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
//                            {
//                                if (databaseError != null)
//                                {
//                                    Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(getActivity(), "Saved!!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        };
//                firebaseReference.child("dishes").setValue(dish, completionListener);

                // not working ! TODO
                dismiss();
                break;
            case R.id.button_cancel_update_dish:
                dismiss();
                break;
        }
    }
}