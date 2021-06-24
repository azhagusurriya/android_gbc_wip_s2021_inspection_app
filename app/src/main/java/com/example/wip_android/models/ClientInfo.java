package com.example.wip_android.models;

import android.widget.ImageView;

public class ClientInfo {
    ImageView clientImage;
    String clientName;
    String clientStreetAddress;
    String clientCity;
    String clientProvince;
    String clientPhoneNumber;

    public ClientInfo() {
    }

    public ClientInfo(String clientName, String clientStreetAddress, String clientCity, String clientProvince, String clientPhoneNumber) {
        this.clientName = clientName;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientProvince = clientProvince;
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public ClientInfo(ImageView clientImage, String clientName, String clientStreetAddress, String clientCity, String clientProvince, String clientPhoneNumber) {
        this.clientImage = clientImage;
        this.clientName = clientName;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientProvince = clientProvince;
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public ImageView getClientImage() {
        return clientImage;
    }

    public void setClientImage(ImageView clientImage) {
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