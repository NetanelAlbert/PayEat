package com.example.payeat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Layout;
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

import com.example.payeat.Dish;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinningPerson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinalBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinalBillFragment extends DialogFragment {

    private ArrayList<DinningPerson> names;
    private int tableNumber;
    private TextView totalSumTextView;

    public FinalBillFragment() {
        // Required empty public constructor
    }


    private void setArguments(ArrayList<DinningPerson> names, int tableNumber){
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
    public static FinalBillFragment newInstance(ArrayList<DinningPerson> names, int tableNumber) {
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

        totalSumTextView = view.findViewById(R.id.fragment_final_bill_total_sum_textView);

    }

    private void updateTip(TextView sum, EditText tip, DinningPerson person, int addToTip, Context context){
        String input = tip.getText().toString();
        int currentTip = input.length() == 0 ? 0 : Integer.parseInt(input);
        int newTip = currentTip + addToTip;

        if(newTip < 0){
            Toast.makeText(context, "אין אפשרות להזין טיפ שלילי", Toast.LENGTH_SHORT).show();
            return;
        }
        double newSum = person.howMuchToPay();
        newSum += newSum*newTip/100;

        DecimalFormat format = new DecimalFormat("##.#");
        sum.setText(format.format(newSum));
        if(addToTip != 0){ // it's mean that chang came from the editText so no change is needed
            tip.setText(String.valueOf(newTip));
        }
        person.setTipPercent(newTip);
        totalSumTextView.setText(format.format(getTotalSum()));

    }

    private double getTotalSum(){
        double ans = 0;
        for(DinningPerson p : names){
            ans += p.howMuchToPay()*(p.getTipPercent()+100)/100;
        }

        return ans;
    }

    private class FinalBillAdapter extends ArrayAdapter<DinningPerson> {


        public FinalBillAdapter(@NonNull Context context, int resource, @NonNull List<DinningPerson> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_final_bill_list_item, parent, false);
            }

            TextView nameTextView = convertView.findViewById(R.id.fragment_final_bill_list_item_name_textView);
            nameTextView.setText(getItem(position).getName());


            final EditText tipEditText = convertView.findViewById(R.id.fragment_final_bill_list_item_tip_editText);

            final TextView sumTextView = convertView.findViewById(R.id.fragment_final_bill_list_item_sum_textView);

            updateTip(sumTextView, tipEditText, getItem(position), getItem(position).getTipPercent(), getContext());


            TextView nis = convertView.findViewById(R.id.fragment_final_bill_list_item__NIS_textView);
            nis.setText(String.valueOf(Character.toChars(0x20AA)));

            ImageButton plus = convertView.findViewById(R.id.fragment_final_bill_list_item_plus_imageButton);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTip(sumTextView, tipEditText, getItem(position), 1, getContext());
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
                            updateTip(sumTextView, tipEditText, getItem(position), 0, getContext());
                            return true;

//                             return false;
                        }
                    });
            tipEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        updateTip(sumTextView, tipEditText, getItem(position), 0, getContext());
                    } else {
                        //v.requestFocus();

                    }
                }
            });

            ListView innerListView = convertView.findViewById(R.id.fragment_final_bill_list_item_sub_listView);


            DishesAdapter adapter = new DishesAdapter(getContext(), R.layout.fragment_final_bill_sublist_item, getItem(position).getSharingDishes());
            innerListView.setAdapter(adapter);

            final View innerLayout = convertView.findViewById(R.id.fragment_final_bill_list_item_inner_layout);
            ViewGroup.LayoutParams layoutParams = innerLayout.getLayoutParams();
            layoutParams.height = (int) 100 * innerListView.getCount();
            innerLayout.setLayoutParams(layoutParams);

            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(innerLayout.getVisibility() == View.GONE)
                        innerLayout.setVisibility(View.VISIBLE);
                    else
                        innerLayout.setVisibility(View.GONE);
                }
            });
            return convertView;
        }

        private class DishesAdapter extends ArrayAdapter<Dish> {


            public DishesAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects) {
                super(context, resource, objects);
            }

            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_final_bill_sublist_item, parent, false);
                }

                TextView dishName = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_name_textView);
                dishName.setText(getItem(position).getName());
                dishName.setText(String.format(getString(R.string.split_bill_dish_name_and_shares_number), getItem(position).getName(), getItem(position).getShares()));

                TextView description = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_adds_textView);
                //TODO chang to information about this specific order dish (i.e. the chosen topics on a pizza)
                description.setText(getItem(position).getDesc());

                TextView price = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_price_textView);
                price.setText(String.valueOf(getItem(position).getPrice()));


                return convertView;
            }

        }
    }
}