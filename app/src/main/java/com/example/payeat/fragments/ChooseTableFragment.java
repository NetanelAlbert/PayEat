package com.example.payeat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.payeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseTableFragment extends DialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String On_Click_Listener = "OnClickListener";

    private EditText editText;
    private Button button;
    private View.OnClickListener mainActivity;



    public ChooseTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clicker - A listener to the button, should be MainActivity.
     * @return A new instance of fragment ChooseTableFragment.
     */
    public static ChooseTableFragment newInstance(View.OnClickListener clicker) {
        ChooseTableFragment fragment = new ChooseTableFragment();
        fragment.setOnClickListener(clicker);
        return fragment;
    }

    private void setOnClickListener(View.OnClickListener clicker) {
        this.mainActivity = clicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choos_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editText = view.findViewById(R.id.fragment_choose_table_editText);
        button = view.findViewById(R.id.fragment_choose_table_Button);
        button.setOnClickListener(mainActivity);
        editText.requestFocus();
        //editText
        super.onViewCreated(view, savedInstanceState);
    }

    public String getTableNumber() throws RuntimeException {
        if(editText == null)
            return null;

        return editText.getText().toString();
    }
}