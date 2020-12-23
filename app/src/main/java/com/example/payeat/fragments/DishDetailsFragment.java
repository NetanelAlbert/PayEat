package com.example.payeat.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.example.payeat.dataObjects.Database;
import com.example.payeat.dataObjects.Dish;
import com.example.payeat.R;
/**
 this fragment opens when viewing a dish in the menu,
 it shows details about the dish- description, price and a photo.
 from here you can go to OrderDishFragment in order to add this dish to your order
 */
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
    private int dish_price;
    private String dish_desc;
    private boolean in_stock;
    private String category;
    private int tableNum;
    private int dish_position;

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
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        tableNum=getArguments().getInt("tableNum");
        dish_name = getArguments().getString("name");
        dish_desc = getArguments().getString("desc");
        dish_price = getArguments().getInt("price");
        in_stock=getArguments().getBoolean("in_stock");
        category=getArguments().getString("category");
        dish_position = getArguments().getInt("dishPosition");

        TextView_dishName.setText(dish_name);
        TextView_dishDesc.setText(dish_desc);
        TextView_dishPrice.setText(String.valueOf(dish_price)+ getString(R.string.new_shekel));
        mode_manager = getArguments().getBoolean("mode_manager");
        if(mode_manager) {
            orderDishButton.setText("עדכן מנה");
        }


        dish_image = convertView.findViewById(R.id.dish_image);
        String dishImageURL = Database.getDishImageURL(category, dish_position);
       // Drawable image = images.get(menuImageURL);
        //if(image == null){ // not in cash
            Database.LoadDishImageFromWeb(dish_image, getActivity(), dishImageURL);
//        } else {
//            imageView.setImageDrawable(image);
//        }


        return convertView;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.order_dish_fragment_button:
                if(mode_manager) {
                    editDishFragment = EditDishFromManagerFragment.newInstance(dish_image.getDrawable());
                    Bundle bundle = new Bundle();
                    bundle.putString("name", dish_name);
                    bundle.putString("desc", dish_desc);
                    bundle.putInt("price", dish_price);
                    bundle.putString("category", category);
                    bundle.putInt("dish_position", tableNum);

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
                    orderFragment = OrderDishFragment.newInstance(this);
                    Dish d= new Dish(dish_name,  dish_price,  dish_desc,  in_stock, 0, "");
                    orderFragment.setDishToOrder(d, tableNum);
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