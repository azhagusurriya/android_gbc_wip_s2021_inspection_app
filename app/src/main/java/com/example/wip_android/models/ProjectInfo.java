package com.example.wip_android.models;

import java.util.ArrayList;
import java.util.Date;

public class ProjectInfo {

    private String referenceId;
    private String imageLink;
    private ArrayList<Double> buttonLocationX = new ArrayList<>();
    private ArrayList<Double> buttonLocationY = new ArrayList<>();
    private ArrayList<String> buttonTitle = new ArrayList<>();
    private String employeeIdOfRegisteration;
    private boolean completion = false;
    private Date dateOfRegistration;
    private Date dateOfCompletion;
    private Date lastUpdated;

    public ProjectInfo() {
    }

    public ProjectInfo(String referenceId, String imageLink, ArrayList<Double> buttonLocationX,
            ArrayList<Double> buttonLocationY, ArrayList<String> buttonTitle, String employeeIdOfRegisteration,
            boolean completion, Date dateOfRegistration, Date dateOfCompletion, Date lastUpdated) {
        this.referenceId = referenceId;
        this.imageLink = imageLink;
        this.buttonLocationX = buttonLocationX;
        this.buttonLocationY = buttonLocationY;
        this.buttonTitle = buttonTitle;
        this.employeeIdOfRegisteration = employeeIdOfRegisteration;
        this.completion = completion;
        this.dateOfRegistration = dateOfRegistration;
        this.dateOfCompletion = dateOfCompletion;
        this.lastUpdated = lastUpdated;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public ArrayList<Double> getButtonLocationX() {
        return buttonLocationX;
    }

    public void setButtonLocationX(ArrayList<Double> buttonLocationX) {
        this.buttonLocationX = buttonLocationX;
    }

    public ArrayList<Double> getButtonLocationY() {
        return buttonLocationY;
    }

    public void setButtonLocationY(ArrayList<Double> buttonLocationY) {
        this.buttonLocationY = buttonLocationY;
    }

    public ArrayList<String> getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(ArrayList<String> buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getEmployeeIdOfRegisteration() {
        return employeeIdOfRegisteration;
    }

    public void setEmployeeIdOfRegisteration(String employeeIdOfRegisteration) {
        this.employeeIdOfRegisteration = employeeIdOfRegisteration;
    }

    public boolean isCompletion() {
        return completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
