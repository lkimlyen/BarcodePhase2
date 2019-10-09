package com.demo.architect.data.model;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupCodeEntity {

    @SerializedName("pOrderID")
    @Expose
    private long orderId;

    @SerializedName("pProductDetailID")
    @Expose
    private long productDetailId;

    @SerializedName("pNumber")
    @Expose
    private double number;

    @SerializedName("pUserID")
    @Expose
    private long userId;

    public GroupCodeEntity(long orderId, long productDetailId, double number, long userId) {
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.number = number;
        this.userId = userId;
    }
}
