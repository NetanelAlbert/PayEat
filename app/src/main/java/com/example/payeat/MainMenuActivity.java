package com.example.payeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        String[] l = {""};
        MenusAdapter adapter = new MenusAdapter(this, R.layout.activity_main_menu_gridview, Arrays.asList("ראשונות","שתיה","עיקריות"));

    }

    private class MenusAdapter extends ArrayAdapter<String>{
        private List<String> list;
        public MenusAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_menu_gridview, parent, false);
            }
            TextView title = convertView.findViewById(R.id.main_menu_item_textView);
            title.setText(list.get(position));

            return convertView;
        }
    }

}