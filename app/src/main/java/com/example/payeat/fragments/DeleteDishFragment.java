package com.example.payeat.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.payeat.Database;
import com.example.payeat.Dish;
import com.example.payeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteDishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteDishFragment extends DialogFragment implements View.OnClickListener {

    private int table_number;
    private int dish_number;

    public DeleteDishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DeleteDishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteDishFragment newInstance() {
        DeleteDishFragment fragment = new DeleteDishFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        table_number = getArguments().getInt("table_number");
        dish_number = getArguments().getInt("child_position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View convertView = inflater.inflate(R.layout.fragment_delete_dish, container, false);

        Button okDeleteButton = convertView.findViewById(R.id.button_delete_dish_ok);
        okDeleteButton.setOnClickListener(this);

        Button cancelDeleteButton = convertView.findViewById(R.id.button_delete_dish_cancel);
        cancelDeleteButton.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_delete_dish_ok:

                // get the dish item using order_number and dish_number and then:
                // need to update the order in the database
                Database.deleteDishFromOrder(table_number, dish_number);

                dismiss();
                break;
            case R.id.button_delete_dish_cancel:
                dismiss();
                break;
        }
    }
}