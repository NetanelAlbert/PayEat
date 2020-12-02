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

    private static String ORDER_NUMBER;
    private static String DISH_NUMBER;

    private int order_number;
    private int dish_number;

    public DeleteDishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param PgroupPosition order number.
     * @param PchildPosition dish number.
     * @return A new instance of fragment DeleteDishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteDishFragment newInstance(int PgroupPosition, int PchildPosition) {
        DeleteDishFragment fragment = new DeleteDishFragment();
        Bundle args = new Bundle();
        args.putInt(ORDER_NUMBER, PgroupPosition);
        args.putInt(DISH_NUMBER, PchildPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_number = getArguments().getInt(ORDER_NUMBER);
            dish_number = getArguments().getInt(DISH_NUMBER);
        }
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
                Database.deleteDishFromOrder(order_number, dish_number);

                dismiss();
                break;
            case R.id.button_delete_dish_cancel:
                dismiss();
                break;
        }
    }
}