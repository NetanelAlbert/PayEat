package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.payeat.Dish;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinningPerson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FinalBillActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);

        // Set up the table number
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
        int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
        TextView tableNumTextView = findViewById(R.id.activity_final_bill_table_number_textView);
        if(tableNumTextView != null){
            tableNumTextView.setText(String.format(getString(R.string.table_number_format), tableNum));
        } else {
            System.out.println("-> tableNumTextView is null");
        }


        ArrayList<DinningPerson> dinningPeople = (ArrayList<DinningPerson>) getIntent().getSerializableExtra("names");
        ListView listView = findViewById(R.id.activity_final_bill_listView);
        FinalBillAdapter adapter = new FinalBillAdapter(this, R.layout.activity_final_bill_list_item, dinningPeople);
        listView.setAdapter(adapter);

        // Set total sum
        double sum = 0;
        for(DinningPerson p : dinningPeople){
            sum += p.howMuchToPay()*(p.getTipPercent()+100)/100;
        }
        TextView sumTV = findViewById(R.id.activity_final_bill_total_sum_textView);
        DecimalFormat format = new DecimalFormat("##.#");
        sumTV.setText(format.format(sum));

        Button goBackToMain = findViewById(R.id.back_to_main_button);
        goBackToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back_to_main_button){

            final Activity activity = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("לא ניתן לחזור למסך זה. \nהאם אתה בטוח?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "כן",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(activity, MainActivity.class);
                            startActivity(intent);
                            // todo prevent user to go back from main activity
                        }
                    });

            builder.setNegativeButton(
                    "חזרה",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

    private class FinalBillAdapter extends ArrayAdapter<DinningPerson>  {


        public FinalBillAdapter(@NonNull Context context, int resource, @NonNull List<DinningPerson> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_final_bill_list_item, parent, false);
            }

            TextView nameTextView = convertView.findViewById(R.id.name_textView);
            nameTextView.setText(getItem(position).getName());

            double sum = getItem(position).howMuchToPay()*(getItem(position).getTipPercent()+100)/100;

            TextView sumTextView = convertView.findViewById(R.id.sum_textView);
            DecimalFormat format = new DecimalFormat("##.#");
            sumTextView.setText(format.format(sum));

            ListView innerListView = convertView.findViewById(R.id.inner_list);
            FinalBillAdapter.DishesAdapter adapter = new FinalBillAdapter.DishesAdapter(getContext(), R.layout.fragment_final_bill_sublist_item, getItem(position).getSharingDishes());
            innerListView.setAdapter(adapter);

            final View innerLayout = convertView.findViewById(R.id.inner_list_layout);
            ViewGroup.LayoutParams layoutParams = innerLayout.getLayoutParams();
            layoutParams.height = 100 * innerListView.getCount();
            innerLayout.setLayoutParams(layoutParams);

            if(getCount() == 1){
                innerLayout.setVisibility(View.VISIBLE);
            }
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

                TextView dishNameTextView = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_name_textView);
                dishNameTextView.setText(getItem(position).getName());
                dishNameTextView.setText(String.format(getString(R.string.split_bill_dish_name_and_shares_number), getItem(position).getName(), getItem(position).getShares()));

                String notes = getItem(position).getNotes();
                if(notes == null || notes.length() == 0){
                    notes = "אין הערות";
                }
                TextView notesTextView = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_notes_textView);
                notesTextView.setText(notes);

                TextView price = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_price_textView);
                price.setText(String.valueOf(getItem(position).getPrice()));


                return convertView;
            }

        }
    }
}