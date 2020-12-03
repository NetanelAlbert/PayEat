package com.example.payeat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.payeat.Database;
import com.example.payeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateCostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCostFragment extends DialogFragment implements View.OnClickListener {

    private String table_number;
    private int dish_number;

    private static EditText editTextNumber_old_cost;
    private EditText editTextNumber_new_cost;

    public UpdateCostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment UpdateCostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateCostFragment newInstance(EditText editText_cost) {
        UpdateCostFragment fragment = new UpdateCostFragment();
        editTextNumber_old_cost = editText_cost;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        table_number = getArguments().getString("table_number");
        dish_number = getArguments().getInt("child_position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View convertView = inflater.inflate(R.layout.fragment_update_cost, container, false);

        Button updateCostButton = convertView.findViewById(R.id.button_update_cost);
        updateCostButton.setOnClickListener(this);

        Button cancelCostButton = convertView.findViewById(R.id.button_cancel_cost);
        cancelCostButton.setOnClickListener(this);

        editTextNumber_new_cost = convertView.findViewById(R.id.editTextNumber_new_cost);
        editTextNumber_new_cost.setText(editTextNumber_old_cost.getText());

        return convertView;
    }

    public String getCost() {
        if( editTextNumber_new_cost== null)
            throw new RuntimeException("Should be called after the fragment is alive.");

        return editTextNumber_new_cost.getText().toString();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_update_cost:
                String new_costS = getCost();
                if(new_costS == null || new_costS.length() == 0)
                    return;
                double new_cost = Double.valueOf(new_costS);
                if(Database.setPrice(table_number, dish_number , new_cost)) {
                    editTextNumber_old_cost.setText(""+new_cost);
                }


                // need to update the database with the new cost and not editText

                dismiss();
                break;
            case R.id.button_cancel_cost:
                dismiss();
                break;
        }

    }
}