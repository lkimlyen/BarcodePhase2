package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimesEntity {

    @SerializedName("ListTimesInput")
    @Expose
    private List<Integer> listTimesInput;

    @SerializedName("ListTimesOutput")
    @Expose
    private List<Integer> listTimesOutput;

    public List<Integer> getListTimesInput() {
        return listTimesInput;
    }

    public List<Integer> getListTimesOutput() {
        return listTimesOutput;
    }
}
