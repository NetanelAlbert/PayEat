package com.example.payeat.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.payeat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDishFromManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDishFromManagerFragment extends DialogFragment {

    private static String DISH_NAME;
    private static String DISH_PRICE;
    private static String DISH_DESC;
    private static String DISH_IMAGE;

    private String dish_name;
    private int dish_price;
    private String dish_desc;

    private ImageView dish_image;

    public EditDishFromManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Pdish_name the dish name.
     * @param Pdish_price the dish price.
     * @param Pdish_desc the dish description.
     * @return A new instance of fragment EditDishFromManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDishFromManagerFragment newInstance(String Pdish_name, int Pdish_price, String Pdish_desc) {
        EditDishFromManagerFragment fragment = new EditDishFromManagerFragment();
        Bundle args = new Bundle();
        args.putString(DISH_NAME, Pdish_name);
        args.putInt(DISH_PRICE, Pdish_price);
        args.putString(DISH_DESC, Pdish_desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dish_name = getArguments().getString(DISH_NAME);
            dish_price = getArguments().getInt(DISH_PRICE);
            dish_desc = getArguments().getString(DISH_DESC);
        }
//        dish_image = getActivity().findViewById(R.id.dish_image);
//        Drawable image = dish_image.getDrawable();
//        dish_image = getView().findViewById(R.id.ImageView_update_dish_image);
//        dish_image.setImageDrawable(image);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_dish_from_manager, container, false);
    }
}