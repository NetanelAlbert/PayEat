package com.example.payeat;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;



public class DishDetailsAdapter extends ArrayAdapter<String> {

        private final String s;
        private final Context context;


        public DishDetailsAdapter(@NonNull Context context, int resource, @NonNull List objects, String s) {
            super(context, resource, objects);
            this.s = s;
            this.context = context;
        }

    public DishDetailsAdapter(@NonNull Context context, int resource, String s, Context context1) {
        super(context, resource);
        this.s = s;
        this.context = context1;
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String curS = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_menu_by_title, parent, false);
            }
            // Lookup view for data population
            TextView dish_name_text = convertView.findViewById(R.id.dish_name_text);
            TextView descriptionText = convertView.findViewById(R.id.dish_detailes_text);
            TextView DishPriceText = convertView.findViewById(R.id.dish_price_text);

            return convertView;
        }
    }

