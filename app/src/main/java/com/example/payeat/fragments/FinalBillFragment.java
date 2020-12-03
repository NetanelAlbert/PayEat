package com.example.payeat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payeat.R;
import com.example.payeat.dataObjects.DinnerPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinalBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinalBillFragment extends DialogFragment {

    private ArrayList<DinnerPerson> names;
    private int tableNumber;

    public FinalBillFragment() {
        // Required empty public constructor
    }


    private void setArguments(ArrayList<DinnerPerson> names, int tableNumber){
        this.names = names;
        this.tableNumber = tableNumber;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param names - the persons who split this bill.
     * @return A new instance of fragment FinalBillFragment.
     */
    public static FinalBillFragment newInstance(ArrayList<DinnerPerson> names, int tableNumber) {
        FinalBillFragment fragment = new FinalBillFragment();
        fragment.setArguments(names, tableNumber);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tableNumTextView = view.findViewById(R.id.fragment_final_bill_table_number_textView);
        tableNumTextView.setText(String.format(getString(R.string.table_number_format), tableNumber));

        ListView listView = view.findViewById(R.id.fragment_final_bill_listView);
        FinalBillAdapter adapter = new FinalBillAdapter(getContext(), R.layout.fragment_final_bill_list_item, names);
        listView.setAdapter(adapter);






    }

    private static void updateTip(TextView sum, EditText tip, DinnerPerson person, int addToTip, Context context){
        String input = tip.getText().toString();
        int currentTip = input.length() == 0 ? 0 : Integer.parseInt(input);
        int newTip = currentTip + addToTip;

        if(newTip < 0){
            Toast.makeText(context, "אין אפשרות להזין טיפ שלילי", Toast.LENGTH_SHORT).show();
            return;
        }
        double newSum = person.howMuchToPay();
        newSum += newSum*newTip/100;

        sum.setText(String.valueOf(newSum));
        if(addToTip != 0){
            tip.setText(String.valueOf(newTip));
        }

    }

    private class FinalBillAdapter extends ArrayAdapter<DinnerPerson> {




        public FinalBillAdapter(@NonNull Context context, int resource, @NonNull List<DinnerPerson> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_final_bill_list_item, parent, false);
            }

            TextView name = convertView.findViewById(R.id.fragment_final_bill_list_item_name_textView);
            name.setText(getItem(position).getName());

            final EditText tipEditText = convertView.findViewById(R.id.fragment_final_bill_list_item_tip_editText);

            final TextView sumTextView = convertView.findViewById(R.id.fragment_final_bill_list_item_sum_textView);

            updateTip(sumTextView,tipEditText, getItem(position), 0, getContext());


            TextView nis = convertView.findViewById(R.id.fragment_final_bill_list_item__NIS_textView);
            nis.setText(String.valueOf(Character.toChars(0x20AA)));

            ImageButton plus = convertView.findViewById(R.id.fragment_final_bill_list_item_plus_imageButton);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTip(sumTextView, tipEditText, getItem(position),  1, getContext());
                }
            });

            ImageButton minus = convertView.findViewById(R.id.fragment_final_bill_list_item_minus_imageButton);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTip(sumTextView, tipEditText, getItem(position), -1, getContext());

                }
            });
            tipEditText.setOnEditorActionListener(
                     new TextView.OnEditorActionListener() {
                         @Override
                         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                             if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                     (actionId == KeyEvent.KEYCODE_ENTER)) {
                                 updateTip(sumTextView, tipEditText, getItem(position), 0, getContext());
                                 return true;
                             }
                             return false;
                         }
                     });
            tipEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if(!hasFocus){
                         updateTip(sumTextView, tipEditText, getItem(position), 0, getContext());
                     } else {
                         v.requestFocus();
                     }
                 }});

            return convertView;
        }
    }
}