package com.example.payeat;

public class Dish {
    private String name;
    private int price;
    private String description;
    private boolean in_stock;
    private String notes;

    public Dish(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.in_stock = true;
        this.notes = "";
    }

    public Dish(String name, int price, String description, boolean in_stock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.in_stock = in_stock;
        this.notes = "";
    }

    public Dish(String name, int price, String description, boolean in_stock, String notes) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.in_stock = in_stock;
        this.notes = notes;
    }

    public String getName(){
        return name;
    }
    public String getDesc(){
        return description;
    }
    public int getPrice(){
        return price;
    }
    public boolean isIn_stock() {
        return in_stock;
    }
    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return
                "שם מנה: '" + name + '\'' +
                ", מחיר: " + price +
                ", תיאור: '" + description + '\'';
    }
}
