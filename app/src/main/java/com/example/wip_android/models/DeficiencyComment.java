package com.example.wip_android.models;

import java.util.Date;

public class DeficiencyComment {
    private String comment;
    private String employeeNameOfRegisteration;
    private Date dateOfRegistration;

    public DeficiencyComment(String comment, String employeeNameOfRegisteration) {
        this.comment = comment;
        this.employeeNameOfRegisteration = employeeNameOfRegisteration;
        this.dateOfRegistration = new Date();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmployeeNameOfRegisteration() {
        return employeeNameOfRegisteration;
    }

    public void setEmployeeNameOfRegisteration(String employeeNameOfRegisteration) {
        this.employeeNameOfRegisteration = employeeNameOfRegisteration;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }
}
