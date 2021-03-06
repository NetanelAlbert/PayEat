package com.example.payeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.payeat.dataObjects.Database;
import com.example.payeat.dataObjects.Dish;
import com.example.payeat.R;

/**
 this fragment opens when viewing a dish in the menu,
 it allows the inviter to add personal notes for the dish and add it to his order
 */
public class OrderDishFragment extends DialogFragment implements View.OnClickListener {
    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ChooseTableFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    private Context context;
    private static Dish dishToOrder;
    private View.OnClickListener dishDetailsFragment;
    private EditText editText;
    private int tableNum;

    public OrderDishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clicker - A listener to the button, should be MainActivity.
     * @return A new instance of fragment ChooseTableFragment.
     */
    public static OrderDishFragment newInstance(View.OnClickListener clicker) {
        OrderDishFragment fragment = new OrderDishFragment();
        fragment.setOnClickListener(clicker);

        return fragment;
    }

    private void setOnClickListener(View.OnClickListener clicker) {
        this.dishDetailsFragment = clicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View convertView = inflater.inflate(R.layout.fragment_order_dish, container, false);
        Button orderButton = convertView.findViewById(R.id.confirm_order_button);
        orderButton.setOnClickListener(this);
        editText = convertView.findViewById(R.id.order_dish_fragment_costumer_request);
        editText.requestFocus();
        return convertView;

    }

    public void setDishToOrder(Dish d, int table){
        dishToOrder=d;
        tableNum=table;

    }
    public String getNotes() {
        if( editText== null)
            return "no notes";
        return editText.getText().toString();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.confirm_order_button:
                String notes = getNotes();
                dishToOrder.setNotes(notes);
                Database.addDishToOrderInProgress(tableNum, dishToOrder);
                Toast.makeText(getActivity(), "סגור, הזמנתי!", Toast.LENGTH_SHORT ).show();
                dismiss();
                break;
        }

    }


}