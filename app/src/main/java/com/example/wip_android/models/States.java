package com.example.wip_android.models;

public class States {

    // Variables
    String name = null;
    boolean selected = false;
    String id;

    // Constructor
    public States(String name, boolean selected, String id) {
        super();
        this.name = name;
        this.selected = selected;
        this.id = id;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
