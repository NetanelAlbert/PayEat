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

    private static String MODE_MANAGER;

    private Context context;
    private TextView TextView_dishName;
    private TextView TextView_dishDesc;
    private TextView TextView_dishPrice;

    private boolean mode_manager;
    private String dish_name;
    private double dish_price;
    private String dish_desc;

    private ImageView dish_image;

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
    public static DishDetailsFragment newInstance() {
        DishDetailsFragment fragment = new DishDetailsFragment();
//        Bundle args = new Bundle();
//        args.putBoolean(MODE_MANAGER, mode);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mode_manager = getArguments().getBoolean(MODE_MANAGER);
//        }
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

        dish_name = getArguments().getString("name");
        dish_desc = getArguments().getString("desc");
        dish_price = getArguments().getDouble("price");

        TextView_dishName.setText(dish_name);
        TextView_dishDesc.setText(dish_desc);
        TextView_dishPrice.setText(String.valueOf(dish_price));

        mode_manager = getArguments().getBoolean("mode_manager");

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
                    editDishFragment = EditDishFromManagerFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", dish_name);
                    bundle.putString("desc", dish_desc);
                    bundle.putDouble("price", dish_price);
                    bundle.putBoolean("mode_manager", mode_manager);
                    editDishFragment.setArguments(bundle);
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