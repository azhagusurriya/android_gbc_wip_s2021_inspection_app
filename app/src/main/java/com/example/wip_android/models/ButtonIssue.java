package com.example.wip_android.models;

import java.util.ArrayList;

public class ButtonIssue {

    // Variables
    private float locationX;
    private float locationY;
    private int title;

    // Constructor
    public ButtonIssue(float locationX, float locationY, int title) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.title = title;
    }

    // Getters and Setters
    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
