package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NumberInput {
    @SerializedName("NumberTotalInput")
    @Expose
    private double numberTotalInput;

    @SerializedName("NumberSuccess")
    @Expose
    private double numberSuccess;

    @SerializedName("NumberWaitting")
    @Expose
    private double numberWaitting;

    @SerializedName("TimesInput")
    @Expose
    private int timesInput;

    public double getNumberTotalInput() {
        return numberTotalInput;
    }

    public double getNumberSuccess() {
        return numberSuccess;
    }

    public double getNumberWaitting() {
        return numberWaitting;
    }

    public int getTimesInput() {
        return timesInput;
    }


}
