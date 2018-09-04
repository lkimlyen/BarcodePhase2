package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListReasonsEntity {
    @SerializedName("Reasons")
    @Expose
    private List<ReasonsEntity> list;

    public List<ReasonsEntity> getList() {
        return list;
    }
}
