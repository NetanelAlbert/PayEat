package com.example.payeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MenuByTitleActivity extends AppCompatActivity {

    ListView manuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_by_title);
        manuList=(ListView) findViewById(R.id.category_menu_list);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("if");
        arrayList.add("you");
        arrayList.add("see");
        arrayList.add("this");
        arrayList.add("list");
        arrayList.add("it");
        arrayList.add("means");
        arrayList.add("I");
        arrayList.add("finally");
        arrayList.add("did");
        arrayList.add("something");
        arrayList.add("right");
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        manuList.setAdapter(arrayAdapter);
    }
}