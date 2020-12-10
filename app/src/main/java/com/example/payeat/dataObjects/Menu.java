package com.example.payeat.dataObjects;

import java.util.ArrayList;

public class Menu {
    String category;
    ArrayList<Dish> dishes;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public Menu(String category, ArrayList<Dish> dishes) {
        this.category = category;
        this.dishes = dishes;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }
}
