package com.example.wip_android.models;

import android.widget.ImageView;

public class GlossaryItem {

    // Variables
    String category;
    String content;
    String department;
    String description;
    String section;

    // Constructors
    public GlossaryItem() {
    };

    public GlossaryItem(String category, String content, String department, String description, String section) {
        this.category = category;
        this.content = content;
        this.department = department;
        this.description = description;
        this.section = section;
    }

    // Getter and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    // Methods
    @Override
    public String toString() {
        return "GlossaryItem{" + "category='" + category + '\'' + ", content='" + content + '\'' + ", department='"
                + department + '\'' + ", description='" + description + '\'' + ", section='" + section + '\'' + '}';
    }
}
