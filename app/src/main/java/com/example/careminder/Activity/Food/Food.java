package com.example.careminder.Activity.Food;
import java.io.Serializable;
public class Food implements Serializable {
    //serialize class - freeze it so you can use it later
    private static final long serialVersionUID = 10L;

    private String name;
    private Double calories;
    private int id;
    private String note;
    private String mealtype;

    //constructors
    public Food() {
    }

    public Food(String name, Double calories, String note, int id) {
        this.name = name;
        this.calories = calories;
        this.id = id;
        this.note = note;
    }
    public Food(String name, Double calories, String type,  String note) {
        this.name = name;
        this.calories = calories;
        this.mealtype = type;
        this.note = note;
    }
    public Food(String name, Double calories, String type) {
        this.name = name;
        this.calories = calories;
        this.mealtype = type;

    }

    //getters and setters
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMealtype() {
        return mealtype;
    }

    public void setMealtype(String type) {
        this.mealtype = type;
    }

}
