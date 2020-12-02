package com.example.payeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.payeat.R;

public class DishDetailsFragment extends DialogFragment  implements View.OnClickListener {
    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link DishDetailsFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String On_Click_Listener = "OnClickListener";

    private static String MODE_MANAGER;

    private Context context;
    private TextView TextView_dishName;
    private TextView TextView_dishDesc;
    private TextView TextView_dishPrice;
    private boolean mode_manager;

//    private View.OnClickListener menuByTitleActivity;
    private OrderDishFragment orderFragment;
    private EditDishFromManagerFragment editDishFragment;



    public DishDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseTableFragment.
     */
    public static DishDetailsFragment newInstance(boolean mode) {
        DishDetailsFragment fragment = new DishDetailsFragment();
//        fragment.setOnClickListener(clicker);
        Bundle args = new Bundle();
        args.putBoolean(MODE_MANAGER, mode);
        fragment.setArguments(args);
        return fragment;
    }

//    private void setOnClickListener(View.OnClickListener clicker) {
//        this.menuByTitleActivity = clicker;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode_manager = getArguments().getBoolean(MODE_MANAGER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View convertView = inflater.inflate(R.layout.fragment_dish_details, container, false);

        Button orderDishButton = convertView.findViewById(R.id.order_dish_fragment_button);
        orderDishButton.setOnClickListener(this);

        TextView_dishName = convertView.findViewById(R.id.dish_name_text_fragment);
        TextView_dishDesc = convertView.findViewById(R.id.dish_details_fragment);
        TextView_dishPrice = convertView.findViewById(R.id.dish_price_fragment);
        String name = getArguments().getString("name");
        System.out.println("\n\n\nname="+name);
        String desc = getArguments().getString("desc");
        String price = getArguments().getString("price");
        TextView_dishName.setText(name);
        TextView_dishDesc.setText(desc);
        TextView_dishPrice.setText(String.valueOf(price));

        if(mode_manager) {
            orderDishButton.setText("עדכן מנה");
        }

        return convertView;
    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        name = view.findViewById(R.id.dish_name_text_fragment);
//        desc=view.findViewById(R.id.dish_detailes_text);
//        price=view.findViewById(R.id.dish_price_text);
//        final Button orderButton = view.findViewById(R.id.order_dish_fragment_button);
//        orderButton.setVisibility(View.VISIBLE);
//        //does not work ???????
//            orderButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    orderFragment = OrderDishFragment.newInstance((View.OnClickListener) this);
//                    FragmentManager fm = getFragmentManager();
//                    fm.beginTransaction()
//                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                            .show(orderFragment)
//                            .commit();
////                    orderFragment.show(getActivity().getSupportFragmentManager()
////                            , "OrderDishFragment");
//
//                }
//            });
//
//        super.onViewCreated(view, savedInstanceState);
//    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.order_dish_fragment_button:
                if(mode_manager) {
                    int cost;
                    try {
                        cost = Integer.getInteger(""+TextView_dishPrice.getText());
                    }
                    catch (Exception exception) {
                        cost = 0;
                    }

                    editDishFragment = EditDishFromManagerFragment.newInstance(""+TextView_dishName.getText(), cost, ""+TextView_dishDesc.getText());
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(editDishFragment)
                            .commit();
                    editDishFragment.show(getActivity().getSupportFragmentManager()
                            , "EditDishFromManagerFragment");
                }
                else {
                    // do whatever you want when you press on "הזמן מנה"

                    orderFragment = OrderDishFragment.newInstance((View.OnClickListener) this);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(orderFragment)
                            .commit();
                    orderFragment.show(getActivity().getSupportFragmentManager()
                            , "OrderDishFragment");
                }
                dismiss();
                break;
        }

    }
}