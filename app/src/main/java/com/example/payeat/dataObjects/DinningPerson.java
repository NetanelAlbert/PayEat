package com.example.payeat.dataObjects;

import com.example.payeat.Dish;

import java.util.ArrayList;


public class DinningPerson {

    private String name;
    private ArrayList<Dish> sharingDishes;
    private int tipPercent;

    public DinningPerson(String name) {
        this.name = name;
        this.sharingDishes = new ArrayList<>();
        this.tipPercent = 10;
    }

    public void addDish(Dish dish){
        sharingDishes.add(dish);
    }

    public boolean removeDish(Dish dish){
        return sharingDishes.remove(dish);
    }

    public double howMuchToPay(){
        double ans = 0;
        for (Dish dish : sharingDishes) {
            ans += dish.getPrice()/dish.getShares();
        }
        return ans;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Dish> getSharingDishes() {
        return sharingDishes;
    }

    public boolean isShare(Dish dish){
        return sharingDishes.contains(dish);
    }

    public int getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(int tipPercent) {
        this.tipPercent = tipPercent;
    }

    public void notifyRemove(){
        for (Dish dish : sharingDishes) {
            dish.decreaseShares();
        }
    }
}
