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

import com.example.payeat.OnFragmentDismissListener;
import com.example.payeat.R;
import com.example.payeat.dataObjects.DinnerPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NamesFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {


    private ArrayList<DinnerPerson> names;
    private NamesAdapter namesAdapter;
    private EditText editText;
    private OnFragmentDismissListener listener;

    public NamesFragment() {
        // Required empty public constructor
    }

    private void setParameters(ArrayList<DinnerPerson> names, OnFragmentDismissListener listener){
        this.names = names;
        this.listener = listener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NamesFragment.
     */
    public static NamesFragment newInstance(ArrayList<DinnerPerson> names, OnFragmentDismissListener listener) {
        NamesFragment fragment = new NamesFragment();
        fragment.setParameters(names, listener);
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
        editText.requestFocus();

        Button buttonOk = view.findViewById(R.id.fragment_names_ok_button);
        buttonOk.setOnClickListener(this);

        Button buttonSkip = view.findViewById(R.id.fragment_names_skip_button);
        buttonSkip.setOnClickListener(this);

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

    public ArrayList<DinnerPerson> getNames(){
        return names;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fragment_names_add_button){
            if(editText.getText().toString().equals(""))
                return;
            String name = editText.getText().toString();
            namesAdapter.add(new DinnerPerson(name));
            editText.setText("");
        } else if (view.getId() == R.id.fragment_names_ok_button){
            dismiss();
        } else if (view.getId() == R.id.fragment_names_skip_button){
            namesAdapter.clear();
            //names.add(getString(R.string.single_share_text)); // TODO maybe need in the SplitBillActivity
            // TODO sent straight to bil summery
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        names.remove(i);
        namesAdapter.notifyDataSetChanged();
        return true;
    }

//    @Override
//    public void dismiss() {
//        listener.notifyDismiss();
//        System.out.println("-->  @Override dismiss()");
//        super.dismiss();
//    }

    @Override
    public void onDestroyView() {
        listener.notifyDismiss();
        super.onDestroyView();
    }

    private class NamesAdapter extends ArrayAdapter<DinnerPerson> {
        public NamesAdapter(@NonNull Context context, int resource, @NonNull List<DinnerPerson> objects) {
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