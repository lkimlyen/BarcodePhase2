package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by uyminhduc on 10/23/16.
 */

public class BaseResponse<T>{


    @SerializedName("Data")
    @Expose
    private T data;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
