package com.example.payeat.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.payeat.DataChangeListener;
import com.example.payeat.Database;
import com.example.payeat.Dish;
import com.example.payeat.R;
import com.example.payeat.activities.MyCartActivity;

import javax.xml.transform.sax.SAXSource;

public class ChangeNotesFragment extends DialogFragment implements View.OnClickListener  {
    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link ChooseTableFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    private Context context;
    private static Dish dishToOrder;
    private View.OnClickListener MyCartActivity;
    private EditText editText;
    private int tableNum;
    private int position;
    private String originNotes;


    public ChangeNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clicker - A listener to the button, should be MainActivity.
     * @return A new instance of fragment ChooseTableFragment.
     */
    public static ChangeNotesFragment newInstance(View.OnClickListener clicker) {
        ChangeNotesFragment fragment = new ChangeNotesFragment();
        return fragment;
    }

    private void setOnClickListener(View.OnClickListener clicker) {
        this.MyCartActivity = clicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View convertView = inflater.inflate(R.layout.fragment_change_notes, container, false);
        Button changeButton = convertView.findViewById(R.id.confirm_notes_button);
        changeButton.setOnClickListener(this);
        editText = convertView.findViewById(R.id.change_notes_fragment_editText);
        editText.setText(originNotes);
        return convertView;

    }

    public void setDishToOrder(Dish d, int table, int position, String OriginNotes){
        dishToOrder=d;
        tableNum=table;
        this.position=position;
        originNotes=OriginNotes;

    }
    public String getNotes() {
        if( editText== null)
            return "no notes";
        return editText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.confirm_notes_button:
                String notes = getNotes();
                dishToOrder.setNotes(notes);
                System.out.println("position "+position+" notes= "+notes);
                ((com.example.payeat.activities.MyCartActivity)getActivity()).setNotes(position, notes);
                Toast.makeText(getActivity(), "שיניתי מאמי", Toast.LENGTH_SHORT ).show();
                dismiss();
                break;
        }

    }


}