package com.example.wip_android.models;

import android.widget.ImageView;

import java.security.Timestamp;

public class ClientInfo {
    private String clientImage;
    private String clientName;
    private String clientStreetAddress;
    private String clientCity;
    private String clientProvince;
    private String clientPhoneNumber;
    private boolean completion;
//    private Timestamp dateOfRegistration;
    private String department;

    public ClientInfo() {
    }

    public ClientInfo(String clientImage, String clientName, String clientStreetAddress, String clientCity,
                      String clientProvince, String clientPhoneNumber, boolean completion,
                      String department) {
        this.clientImage = clientImage;
        this.clientName = clientName;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientProvince = clientProvince;
        this.clientPhoneNumber = clientPhoneNumber;
        this.completion = completion;
//        this.dateOfRegistration = dateOfRegistration;
        this.department = department;
    }

//    public ClientInfo(String clientImage, String clientName, String clientStreetAddress, String clientCity, String clientProvince, String clientPhoneNumber) {
//        this.clientImage = clientImage;
//        this.clientName = clientName;
//        this.clientStreetAddress = clientStreetAddress;
//        this.clientCity = clientCity;
//        this.clientProvince = clientProvince;
//        this.clientPhoneNumber = clientPhoneNumber;
//    }

    public void setClientImage(String clientImage) {
        this.clientImage = clientImage;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientStreetAddress() {
        return clientStreetAddress;
    }

    public void setClientStreetAddress(String clientStreetAddress) {
        this.clientStreetAddress = clientStreetAddress;
    }

    public String getClientCity() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    public String getClientProvince() {
        return clientProvince;
    }

    public void setClientProvince(String clientProvince) {
        this.clientProvince = clientProvince;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientImage() {
        return clientImage;
    }

    public boolean isCompletion() {
        return completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

//    public Timestamp getDateOfRegistration() {
//        return dateOfRegistration;
//    }
//
//    public void setDateOfRegistration(Timestamp dateOfRegistration) {
//        this.dateOfRegistration = dateOfRegistration;
//    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "clientImage=" + clientImage +
                ", clientName='" + clientName + '\'' +
                ", clientStreetAddress='" + clientStreetAddress + '\'' +
                ", clientCity='" + clientCity + '\'' +
                ", clientProvince='" + clientProvince + '\'' +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                '}';
    }

}
