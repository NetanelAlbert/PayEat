package com.example.payeat;

import java.util.ArrayList;


public class DinnerPerson {

    private String name;
    private ArrayList<Dish> sharingDishes;

    public DinnerPerson(String name) {
        this.name = name;
        sharingDishes = new ArrayList<>();
    }

    public void addDish(Dish dish){
        sharingDishes.add(dish);
    }

    public boolean removeDish(Dish dish){
        return sharingDishes.remove(dish);
    }

    public double howMuch(){
        double ans = 0;
        for (Dish dish : sharingDishes) {
            ans += dish.getPrice()/dish.getShares();
        }
        return ans;
    }
}
