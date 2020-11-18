package com.example.payeat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateCostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCostFragment extends DialogFragment {

//
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//
//    private String mParam1;
//    private String mParam2;
    private Button button_update_cost;
    private Button button_cancel_cost;
    private EditText editText;
    private View.OnClickListener ExpandableListViewAdapter;

    public UpdateCostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clicker - A listener to the button, should be ExpandableListViewAdapter.
     * @return A new instance of fragment UpdateCostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateCostFragment newInstance(View.OnClickListener clicker) {
        UpdateCostFragment fragment = new UpdateCostFragment();
        fragment.setOnClickListener(clicker);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    private void setOnClickListener(View.OnClickListener clicker) {
        this.ExpandableListViewAdapter = clicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View convertView = inflater.inflate(R.layout.fragment_update_cost, container, false);

        Button updateCostButton = convertView.findViewById(R.id.button_update_cost);
        updateCostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = convertView.findViewById(R.id.editText_cost); //this return null
                String new_costS = getCost();
                if(new_costS == null || new_costS.length() == 0)
                    return;
                int new_cost = Integer.parseInt(new_costS);
                editText.setText(new_cost);
                dismiss();
            }
        });

        Button cancelCostButton = convertView.findViewById(R.id.button_cancel_cost);
        cancelCostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return convertView;
    }

    public String getCost() {
        if(editText == null)
            throw new RuntimeException("Should be called after the fragment is alive.");

        return editText.getText().toString();
    }
}