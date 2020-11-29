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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.payeat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NamesFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {


    private ArrayList<String> names = new ArrayList<>();
    private NamesAdapter namesAdapter;
    private EditText editText;

    public NamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NamesFragment newInstance() {
        NamesFragment fragment = new NamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button addName = view.findViewById(R.id.fragment_names_add_button);
        addName.setOnClickListener(this);
        editText = view.findViewById(R.id.fragment_names_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addName.callOnClick();
                return true;
            }
        });

        ListView listView = view.findViewById(R.id.fragment_names_listView);
        namesAdapter = new NamesAdapter(getContext(), R.layout.simple_text_view, names);
        listView.setAdapter(namesAdapter);
        listView.setOnItemLongClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_names, container, false);
    }

    public ArrayList<String> getNames(){
        return names;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_names_add_button){
            if(editText.getText().toString().equals(""))
                return;
            namesAdapter.add(editText.getText().toString());
            editText.setText("");
        } else if (view.getId() == R.id.fragment_names_skip_button || view.getId() == R.id.fragment_names_ok_button){
            if(view.getId() == R.id.fragment_names_skip_button){
                names.clear();
                names.add(getString(R.string.single_share_text));
            }
            dismiss();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        names.remove(i);
        namesAdapter.notifyDataSetChanged();
        return true;
    }


    private class NamesAdapter extends ArrayAdapter<String> {
        public NamesAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_text_view, parent, false);
            }
            TextView title = convertView.findViewById(R.id.simple_text_view);
            title.setText(getItem(position));

            return convertView;
        }
    }



}