//package com.example.payeat;
//
//
//import android.content.Context;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//
//
//public class DishDetailsAdapter extends ArrayAdapter<DishDetail> {
//
//        private final PayEat pi;
//        private final Context context;
//
//        public DetailsAdapter(@NonNull Context context, int resource, @NonNull List objects, PayEat pi) {
//            super(context, resource, objects);
//            this.pi = pi;
//            this.context = context;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // Get the data item for this position
//            PayEat pe = getItem(position);
//            // Check if an existing view is being reused, otherwise inflate the view
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_menu_by_title, parent, false);
//            }
//            // Lookup view for data population
//            TextView dish_name_text = convertView.findViewById(R.id.dish_name_text);
//            TextView descriptionText = convertView.findViewById(R.id.dish_detailes_text);
//            TextView DishPriceText = convertView.findViewById(R.id.dish_price_text);
//            //delete the .00 if it is a round number
////            double sum = expenseDao.getAmount();
////            String sumString = String.valueOf(sum);
////            if (sum % 1 == 0) {
////                sumString = String.valueOf((int) sum);
////            }
////            // Populate the data into the template view using the data object
////            dateTextView.setText(date);
////            descriptionTextView.setText(expenseDao.getDescription());
////            sumTextView.setText(sumString);
//            // Return the completed view to render on screen
//            return convertView;
//        }
//    }
//
