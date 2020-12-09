package com.example.payeat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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

        // rest of the views
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
        namesIndex = 0;
        if(names.isEmpty()){
            nameTextView.setText("לחץ כדי להוסיף שמות");
            billAdapter.notifyDataSetChanged(null);
        } else {
            if(names.size() == 1){ // single customer - no sharing is need
                for(int i = 0; i < order.getOrderInfo().size(); i++){
                    onToggleClick(i, true);
                }
            }
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

        if(person.isShare(dish) == isOn){
            return false;
        }

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

        final OnToggleClickListener switchListener;
        private DinningPerson person;

        public BillAdapter(@NonNull Context context, int resource, @NonNull List<Dish> objects, OnToggleClickListener toggleListener) {
            super(context, resource, objects);
            this.switchListener = toggleListener;
        }

        public void notifyDataSetChanged(DinningPerson person){
            this.person = person;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_split_bill_list_item, parent, false);
            }
            TextView dishName = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_name_textView);
            dishName.setText(getItem(position).getName());

            TextView shares = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_shares_textView);
            shares.setText(String.valueOf(getItem(position).getShares()));

            TextView notesTextView = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_notes_textView);
            String notes = getItem(position).getNotes();
            if(notes == null || notes.length() == 0){
                notes = "אין הערות";
            }
            notesTextView.setText(notes);

            TextView price = convertView.findViewById(R.id.activity_split_bill_sublist_item_dish_price_textView);
            price.setText(String.valueOf(getItem(position).getPrice()));

            final SwitchCompat onOff = convertView.findViewById(R.id.split_bill_list_item_switch);

            final ArrayAdapter<Dish> adapter = this;
            onOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchCompat buttonView = (SwitchCompat) v;
                    boolean hasPerson = switchListener.onToggleClick(position, buttonView.isChecked());
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
            String switchText = isShare ? "שלי" : "לא שלי";
            onOff.setText(switchText);


            return convertView;
        }
    }
}