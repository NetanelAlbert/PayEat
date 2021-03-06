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
import android.widget.Toast;

import com.example.payeat.interfaces.OnFragmentDismissListener;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinningPerson;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NamesFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    public static enum Type {
        names, email
    };

    private ArrayList<DinningPerson> names;
    private NamesAdapter namesAdapter;
    private EditText editText;
    private OnFragmentDismissListener listener;
    private Type type;

    public NamesFragment() {
        // Required empty public constructor
    }

    private void setParameters(ArrayList<DinningPerson> names, OnFragmentDismissListener listener, Type type){
        this.names = names;
        this.listener = listener;
        this.type = type;
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NamesFragment.
     */
    public static NamesFragment newInstance(ArrayList<DinningPerson> names, OnFragmentDismissListener listener, Type type) {
        NamesFragment fragment = new NamesFragment();
        fragment.setParameters(names, listener, type);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button addName;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button addName = view.findViewById(R.id.fragment_names_add_button);
        addName.setOnClickListener(this);
        this.addName = addName;
        editText = view.findViewById(R.id.fragment_names_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addName.callOnClick();
                return true;
            }
        });
        editText.requestFocus();

        Button buttonOk = view.findViewById(R.id.fragment_names_ok_button);
        buttonOk.setOnClickListener(this);


        ListView listView = view.findViewById(R.id.fragment_names_listView);
        namesAdapter = new NamesAdapter(getContext(), R.layout.simple_text_view, names);
        listView.setAdapter(namesAdapter);
        listView.setOnItemLongClickListener(this);

        if(type == Type.email){
            TextView title = view.findViewById(R.id.fragment_names_textView);
            title.setText("הכנס כתובת מייל אחת או יותר");
        }

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_names, container, false);
    }

    public ArrayList<DinningPerson> getNames(){
        return names;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_names_add_button){
            if(editText.getText().toString().equals(""))
                return;
            String name = editText.getText().toString();
            if(type == Type.email && !isValidEmailAddress(name)) {
                Toast.makeText(getContext(), "נא להכניס כתובת מייל תקינה", Toast.LENGTH_SHORT).show();
                return;
            }
            namesAdapter.add(new DinningPerson(name));
            editText.setText("");
        } else if (view.getId() == R.id.fragment_names_ok_button){
            addName.performClick();
            dismiss();
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        names.remove(i).notifyRemove();
        namesAdapter.notifyDataSetChanged();
        return true;
    }


    @Override
    public void onDestroyView() {
        listener.notifyDismiss();
        super.onDestroyView();
    }

    private class NamesAdapter extends ArrayAdapter<DinningPerson> {
        public NamesAdapter(@NonNull Context context, int resource, @NonNull List<DinningPerson> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_text_view, parent, false);
            }
            TextView title = convertView.findViewById(R.id.simple_text_view);
            title.setText(getItem(position).getName());

            return convertView;
        }
    }



}