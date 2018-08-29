package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentEntity {

    @SerializedName("ApartmentID")
    @Expose
    private int apartmentID;

    @SerializedName("ApartmentName")
    @Expose
    private String apartmentName;

    public int getApartmentID() {
        return apartmentID;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    @Override
    public String toString() {
        return apartmentName;
    }
}
