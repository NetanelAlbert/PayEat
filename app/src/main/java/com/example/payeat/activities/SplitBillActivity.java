package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.payeat.Dish;
import com.example.payeat.OnFragmentDismissListener;
import com.example.payeat.OnToggleClickListener;
import com.example.payeat.Order;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinnerPerson;
import com.example.payeat.fragments.NamesFragment;

import java.util.ArrayList;
import java.util.List;

public class SplitBillActivity extends AppCompatActivity implements OnFragmentDismissListener, View.OnClickListener, OnToggleClickListener {

    private Order order;
    private ArrayList<DinnerPerson> names;
    private int namesIndex;
    private TextView nameTextView;
    private BillAdapter billAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);


        names = new ArrayList<>();
        NamesFragment namesFragment = NamesFragment.newInstance(names, this);
        namesFragment.show(getSupportFragmentManager(), "Names Fragment");

        // Set up the table number
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
        int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
        TextView tableNumTextView = findViewById(R.id.activity_split_bill_table_number_textView);
        tableNumTextView.setText("שולחן "+tableNum);


        // Set up the list
        // TODO get the real order (maybe with other dish object to represent the add ones)
        Dish[] dishes = {new Dish("Toast", 21.9, "Great toast"),
                        new Dish("Lemonade", 12, "Cold lemonade"),
                        new Dish("Big french fries", 18.5, "Big and tasty bole of chips")};
        order = new Order(dishes, 2);

        ListView listView = findViewById(R.id.activity_split_bill_listView);
        billAdapter = new BillAdapter(this,R.layout.activity_split_bill_list_item,order.getOrderInfo(),this);
        listView.setAdapter(billAdapter);

        nameTextView = findViewById(R.id.activity_split_bill_name_textView);
        nameTextView.setOnClickListener(this);
        Button next = findViewById(R.id.activity_split_bill_next_button);
        next.setOnClickListener(this);
        Button prev = findViewById(R.id.activity_split_bill_prev_button);
        prev.setOnClickListener(this);

    }

    @Override
    public void notifyDismiss() {
        if(names.isEmpty()){
            nameTextView.setText("לחץ כדי לערוך שמות");
        } else {
            namesIndex = 0;
            nameTextView.setText(names.get(namesIndex).getName());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_split_bill_next_button){
            if(namesIndex < names.size()-1){
                DinnerPerson person = names.get(++namesIndex);
                nameTextView.setText(person.getName());
                billAdapter.notifyDataSetChanged(person);
            } else {
                Toast.makeText(this,"זהו השם האחרון", Toast.LENGTH_SHORT).show();
            }

        } else if(v.getId() == R.id.activity_split_bill_prev_button){
            if(namesIndex > 0){
                DinnerPerson person = names.get(--namesIndex);
                nameTextView.setText(person.getName());
                billAdapter.notifyDataSetChanged(person);
            } else {
                Toast.makeText(this,"זהו השם הראשון", Toast.LENGTH_SHORT).show();
            }

        } else if(v.getId() == R.id.activity_split_bill_name_textView){
            NamesFragment namesFragment = NamesFragment.newInstance(names, this);
            namesFragment.show(getSupportFragmentManager(), "Names Fragment");
        }
    }

    @Override
    public boolean onToggleClick(int index, boolean isOn) {
        if(names.size() < 2){
            Toast.makeText(this,"אנא הכנס לפחות 2 שמות על מנת לחלק את החשבון", Toast.LENGTH_LONG).show();
            return false;
        }
        Dish dish = order.get(index);
        DinnerPerson person = names.get(namesIndex);
        if(isOn){
            dish.increaseShares();
            person.addDish(dish);
        }
        else {
            dish.decreaseShares();
            person.removeDish(dish);
        }
        return  true;
    }


    private class BillAdapter extends ArrayAdapter<Dish> {

        final OnToggleClickListener toggleListener;
        private DinnerPerson person;

        public BillAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects, OnToggleClickListener toggleListener) {
            super(context, resource, objects);
            this.toggleListener = toggleListener;
        }

        public void notifyDataSetChanged(DinnerPerson person){
            this.person = person;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_split_bill_list_item, parent, false);
            }
            TextView dishName = convertView.findViewById(R.id.split_bill_list_item_dish_name_textView);
            dishName.setText(getItem(position).getName());
            dishName.setText(String.format(getString(R.string.split_bill_dish_name_and_shares_number), getItem(position).getName(), getItem(position).getShares()));

            TextView description = convertView.findViewById(R.id.split_bill_list_item_dish_adds_textView);
            //TODO chang to information about this specific order dish (i.e. the chosen topics on a pizza)
            description.setText(getItem(position).getDesc());

            TextView price = convertView.findViewById(R.id.split_bill_list_item_dish_price_textView);
            price.setText(String.valueOf(getItem(position).getPrice()));

            final ToggleButton onOff = convertView.findViewById(R.id.split_bill_list_item_toggle);
//            onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    boolean hasPerson = toggleListener.onToggleClick(position, isChecked);
//                    if(!hasPerson){
//                        buttonView.setChecked(false);
//                    }
//                }
//            });
            final ArrayAdapter<Dish> adapter = this;
            onOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToggleButton buttonView = (ToggleButton) v;
                    boolean hasPerson = toggleListener.onToggleClick(position, buttonView.isChecked());
                    if (!hasPerson) {
                        buttonView.setChecked(false);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            // Set up toggle state
            if(person != null){
                ToggleButton toggle = convertView.findViewById(R.id.split_bill_list_item_toggle);
                boolean isShare = person.isShare(getItem(position));
                toggle.setChecked(isShare);

            }

            return convertView;
        }
    }
}