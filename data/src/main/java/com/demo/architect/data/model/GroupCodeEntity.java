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
    private int orderId;

    @SerializedName("pProductDetailID")
    @Expose
    private int productDetailId;

    @SerializedName("pNumber")
    @Expose
    private int number;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    public GroupCodeEntity(int orderId, int productDetailId, int number, int userId) {
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.number = number;
        this.userId = userId;
    }
}
