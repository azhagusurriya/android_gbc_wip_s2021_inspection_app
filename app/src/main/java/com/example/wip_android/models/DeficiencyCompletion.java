package com.example.wip_android.models;

public class DeficiencyCompletion {

    // Variables
    private String name;
    private boolean isDone;

    // Constructors
    public DeficiencyCompletion() {
    }

    public DeficiencyCompletion(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
