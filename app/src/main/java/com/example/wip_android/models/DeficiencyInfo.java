package com.example.wip_android.models;

import java.util.Date;

public class DeficiencyInfo {

    // Variables
    private String title;
    private String imageLinkBefore;
    private String imageLinkAfter;
    private String commentBefore;
    private String commentAfter;
    private String employeeIdOfRegisteration;
    private boolean completion = false;
    private Date dateOfRegistration;
    private Date dateOfCompletion;
    private Date lastUpdated;

    // Constructors
    public DeficiencyInfo() {
    }

    public DeficiencyInfo(String title, String imageLinkBefore, String imageLinkAfter, String commentBefore,
            String commentAfter, String employeeIdOfRegisteration, boolean completion, Date dateOfRegistration,
            Date dateOfCompletion, Date lastUpdated) {
        this.title = title;
        this.imageLinkBefore = imageLinkBefore;
        this.imageLinkAfter = imageLinkAfter;
        this.commentBefore = commentBefore;
        this.commentAfter = commentAfter;
        this.employeeIdOfRegisteration = employeeIdOfRegisteration;
        this.completion = completion;
        this.dateOfRegistration = dateOfRegistration;
        this.dateOfCompletion = dateOfCompletion;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLinkBefore() {
        return imageLinkBefore;
    }

    public void setImageLinkBefore(String imageLinkBefore) {
        this.imageLinkBefore = imageLinkBefore;
    }

    public String getImageLinkAfter() {
        return imageLinkAfter;
    }

    public void setImageLinkAfter(String imageLinkAfter) {
        this.imageLinkAfter = imageLinkAfter;
    }

    public String getCommentBefore() {
        return commentBefore;
    }

    public void setCommentBefore(String commentBefore) {
        this.commentBefore = commentBefore;
    }

    public String getCommentAfter() {
        return commentAfter;
    }

    public void setCommentAfter(String commentAfter) {
        this.commentAfter = commentAfter;
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
