package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserEntity {
    @SerializedName("ID")
    @Expose
    private long id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Role")
    @Expose
    private int role;

    @SerializedName("UserType")
    @Expose
    private String userType;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRole() {
        return role;
    }

    public String getUserType() {
        return userType;
    }
}
