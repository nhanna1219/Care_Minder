package com.example.careminder.Activity.Food;
import java.io.Serializable;
public class Food implements Serializable {
    //serialize class - freeze it so you can use it later
    private static final long serialVersionUID = 10L;

    private String name;
    private int calories;
    private int id;
    private String recordDate;

    //constructors
    public Food() {
    }

    public Food(String name, int calories, String recordDate, int id) {
        this.name = name;
        this.calories = calories;
        this.id = id;
        this.recordDate = recordDate;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

}
