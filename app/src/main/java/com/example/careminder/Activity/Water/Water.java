package com.example.careminder.Activity.Water;

import java.io.Serializable;

public class Water implements Serializable {
    //serialize class - freeze it so you can use it later
    private static final long serialVersionUID = 10L;

    private int ml;

    private int id;
    private String recordDate;

    //constructors
    public Water() {
    }

    public Water(int ml, String recordDate, int id) {
        this.ml = ml;
        this.id = id;
        this.recordDate = recordDate;
    }

    //getters and setters
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
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
