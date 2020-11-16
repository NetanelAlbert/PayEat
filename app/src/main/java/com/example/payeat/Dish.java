package com.example.payeat;

public class Dish {
    private String name;
    private int price;
    private String description;


    public Dish(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName(){
        return name;
    }
    public void setName(String n){
        name=n;
    }
    public String getDesc(){
        return description;
    }
    public void setDesc(String d){
        description=d;
    }
    public int getPrice(){
        return price;
    }
    public void setName(int p){
        price=p;
    }


}
