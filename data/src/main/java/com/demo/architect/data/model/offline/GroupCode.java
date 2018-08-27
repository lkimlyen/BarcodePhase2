package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupCode extends RealmObject {
    @PrimaryKey
    private int id;

    private String groupCode;

    @SerializedName("pOrderID")
    @Expose
    private int orderId;

    private String module;
    @SerializedName("pProductDetailID")
    @Expose
    private int productDetailId;

    private String productDetailName;

    @SerializedName("pNumber")
    @Expose
    private int number;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    private String dateGroup;
    private int status;

    public GroupCode() {
    }

    public GroupCode(int id, int orderId, int productDetailId, int number, int userId, String dateGroup, int status) {
        this.id = id;
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.number = number;
        this.userId = userId;
        this.dateGroup = dateGroup;
        this.status = status;
    }


    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(GroupCode.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, int orderId, int productDetailId, int number, int userId) {
        GroupCode groupCode = new GroupCode(id(realm) + 1, orderId, productDetailId, number, userId,
                DateUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
        realm.copyToRealm(groupCode);
    }


    public String getProductDetailName() {
        return productDetailName;
    }

    public void setProductDetailName(String productDetailName) {
        this.productDetailName = productDetailName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateGroup() {
        return dateGroup;
    }

    public void setDateGroup(String dateGroup) {
        this.dateGroup = dateGroup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
