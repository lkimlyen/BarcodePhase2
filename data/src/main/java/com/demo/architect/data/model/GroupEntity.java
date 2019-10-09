package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupEntity {
    @SerializedName("MasterGroupID")
    @Expose
    private long masterGroupId;

    @SerializedName("OrderID")
    @Expose
    private long orderId;

    @SerializedName("GroupCode")
    @Expose
    private String groupCode;

    @SerializedName("RowVersion")
    @Expose
    private int rowVersion;

    @SerializedName("ListDetail")
    @Expose
    private List<ProductGroupEntity> producGroupList;

    public long getMasterGroupId() {
        return masterGroupId;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public int getRowVersion() {
        return rowVersion;
    }

    public List<ProductGroupEntity> getProducGroupList() {
        return producGroupList;
    }
}
