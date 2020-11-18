package com.example.payeat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class DishDetailsFragment extends DialogFragment  implements AdapterView.OnItemClickListener {
    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link DishDetailsFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String On_Click_Listener = "OnClickListener";

    private Context context;
    private TextView name;
    private TextView desc;
    private TextView price;

    private View.OnClickListener menuByTitleActivity;
    private OrderDishFragment orderFragment;



    public DishDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clicker - A listener to the button, should be MainActivity.
     * @return A new instance of fragment ChooseTableFragment.
     */
    public static DishDetailsFragment newInstance(View.OnClickListener clicker) {
        DishDetailsFragment fragment = new DishDetailsFragment();
        fragment.setOnClickListener(clicker);
        return fragment;
    }

    private void setOnClickListener(View.OnClickListener clicker) {
        this.menuByTitleActivity = clicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dish_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        name = view.findViewById(R.id.dish_name_text_fragment);
        desc=view.findViewById(R.id.dish_detailes_text);
        price=view.findViewById(R.id.dish_price_text);
        final Button orderButton = view.findViewById(R.id.order_dish_fragment_button);
            orderButton.setVisibility(View.VISIBLE);
            //does not work ???????
//            orderButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    orderFragment = OrderDishFragment.newInstance((View.OnClickListener) this);
//                    orderFragment.show(getSupportFragmentManager(), "OrderDishFragment");
//
//                }
//            });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}