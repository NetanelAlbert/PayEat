package com.example.payeat;

import java.io.Serializable;

public class Dish implements Serializable {
    // For menu
    private String name;
    private int price;
    private String description;

    // For orders
    private int shares = 0;
    private String notes;

    public Dish() {

    }

    public Dish(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.shares = 0;
        this.notes = "";
    }

    public Dish(String name, int price, String description, boolean in_stock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.shares = 0;
        this.notes = "";
    }

    public Dish(String name, int price, String description, boolean in_stock, int shares) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.shares = shares;
        this.notes = "";
    }

    public Dish(String name, int price, String description, boolean in_stock, int shares, String notes) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.shares = shares;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", shares=" + shares +
                ", notes='" + notes + '\'' +
                '}';
    }

    public synchronized void increaseShares(){
        ++shares;
    }
    public synchronized void decreaseShares(){
        --shares;
    }
}