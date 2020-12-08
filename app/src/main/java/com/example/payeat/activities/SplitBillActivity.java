package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.payeat.Database;
import com.example.payeat.Dish;
import com.example.payeat.OnFragmentDismissListener;
import com.example.payeat.OnToggleClickListener;
import com.example.payeat.Order;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinningPerson;
import com.example.payeat.fragments.FinalBillFragment;
import com.example.payeat.fragments.NamesFragment;

import java.util.ArrayList;
import java.util.List;

public class SplitBillActivity extends AppCompatActivity implements OnFragmentDismissListener, View.OnClickListener, OnToggleClickListener {

    private Order order;
    private ArrayList<DinningPerson> names;
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
        tableNumTextView.setText(String.format(getString(R.string.table_number_format), tableNum));


        // Set up the list

        ArrayList<Dish> dishes = Database.getLiveOrder(tableNum);
        order = new Order(dishes, tableNum);

        ListView listView = findViewById(R.id.activity_split_bill_listView);
        billAdapter = new BillAdapter(this,R.layout.activity_split_bill_list_item,order.getOrderInfo(),this);
        listView.setAdapter(billAdapter);

        nameTextView = findViewById(R.id.activity_split_bill_name_textView);
        nameTextView.setOnClickListener(this);

        Button next = findViewById(R.id.activity_split_bill_next_button);
        next.setOnClickListener(this);

        Button prev = findViewById(R.id.activity_split_bill_prev_button);
        prev.setOnClickListener(this);

        Button finalBill = findViewById(R.id.activity_split_bill_final_bill_button);
        finalBill.setOnClickListener(this);

    }

    @Override
    public void notifyDismiss() {
        if(names.isEmpty()){
            nameTextView.setText("לחץ כדי להוסיף שמות");
            billAdapter.notifyDataSetChanged(null);
        } else {
            namesIndex = 0;
            nameTextView.setText(names.get(namesIndex).getName());
            billAdapter.notifyDataSetChanged(names.get(namesIndex));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_split_bill_next_button){
            if(namesIndex < names.size()-1){
                DinningPerson person = names.get(++namesIndex);
                nameTextView.setText(person.getName());
                billAdapter.notifyDataSetChanged(person);
            } else {
                Toast.makeText(this,"זהו השם האחרון", Toast.LENGTH_SHORT).show();
            }

        } else if(v.getId() == R.id.activity_split_bill_prev_button){
            if(namesIndex > 0){
                DinningPerson person = names.get(--namesIndex);
                nameTextView.setText(person.getName());
                billAdapter.notifyDataSetChanged(person);
            } else {
                Toast.makeText(this,"זהו השם הראשון", Toast.LENGTH_SHORT).show();
            }

        } else if(v.getId() == R.id.activity_split_bill_name_textView){
            NamesFragment namesFragment = NamesFragment.newInstance(names, this);
            namesFragment.show(getSupportFragmentManager(), "Names Fragment");

        } else if (v.getId() == R.id.activity_split_bill_final_bill_button) {
            //todo add condition that all the dishes marked
            for(Dish dish : order.getOrderInfo()){
                if(dish.getShares() == 0){
                    Toast.makeText(this,"יש לסמן לפחות סועד אחד עבור כל מנה", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key), MODE_PRIVATE);
            int tableNum = preferences.getInt(getString(R.string.client_table_number),-1);
            FinalBillFragment fragment = FinalBillFragment.newInstance(names, tableNum);
            fragment.show(getSupportFragmentManager(), "FinalBillFragment");
        }
    }

    @Override
    public boolean onToggleClick(int index, boolean isOn) {
        if(names.size() == 0){
            Toast.makeText(this,"אנא הכנס שמות על מנת לחלק את החשבון", Toast.LENGTH_LONG).show();
            return false;
        }
        Dish dish = order.get(index);
        DinningPerson person = names.get(namesIndex);
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
        private DinningPerson person;

        public BillAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects, OnToggleClickListener toggleListener) {
            super(context, resource, objects);
            this.toggleListener = toggleListener;
        }

        public void notifyDataSetChanged(DinningPerson person){
            this.person = person;
            notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if(getCount() == 1){ // single customer - no sharing is need
                for(int i = 0; i < getCount(); i++){
                    onToggleClick(i, true);
                }
                super.notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_split_bill_list_item, parent, false);
            }
            TextView dishName = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_name_textView);
            dishName.setText(getItem(position).getName());
            dishName.setText(String.format(getString(R.string.split_bill_dish_name_and_shares_number), getItem(position).getName(), getItem(position).getShares()));

            TextView description = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_adds_textView);
            //TODO chang to information about this specific order dish (i.e. the chosen topics on a pizza)
            description.setText(getItem(position).getDescription());

            TextView price = convertView.findViewById(R.id.fragment_final_bill_sublist_item_dish_price_textView);
            price.setText(String.valueOf(getItem(position).getPrice()));

            final ToggleButton onOff = convertView.findViewById(R.id.split_bill_list_item_toggle);

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
            boolean isShare = (person != null && person.isShare(getItem(position)));
            onOff.setChecked(isShare);

            return convertView;
        }
    }
}