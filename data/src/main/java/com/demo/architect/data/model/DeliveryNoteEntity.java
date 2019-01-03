package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryNoteEntity {
    @SerializedName("ID")
    @Expose
    private long id;

    @SerializedName("MaPhieu")
    @Expose
    private String noteCode;

    public long getId() {
        return id;
    }

    public String getNoteCode() {
        return noteCode;
    }

    @Override
    public String toString() {
        return noteCode;
    }
}
