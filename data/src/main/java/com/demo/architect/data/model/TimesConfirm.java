package com.demo.architect.data.model;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.offline.LogScanConfirm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimesConfirm extends RealmObject {
    @PrimaryKey
    private int id;
    private int orderId;
    private int departmentIdOut;
    private int times;
    private boolean checkedAll;
    private int userId;
    private int status;

    public TimesConfirm() {
    }

    public TimesConfirm(int id, int orderId, int departmentIdOut, int times, boolean checkedAll, int userId, int status) {
        this.id = id;
        this.orderId = orderId;
        this.departmentIdOut = departmentIdOut;
        this.times = times;
        this.checkedAll = checkedAll;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDepartmentIdOut() {
        return departmentIdOut;
    }

    public void setDepartmentIdOut(int departmentIdOut) {
        this.departmentIdOut = departmentIdOut;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean isCheckedAll() {
        return checkedAll;
    }

    public void setCheckedAll(boolean checkedAll) {
        this.checkedAll = checkedAll;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(TimesConfirm.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static boolean getCheckedConfirmAll(Realm realm, int orderId, int departmentId, int times, int userId) {
        TimesConfirm timesConfirm = realm.where(TimesConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIdOut", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return timesConfirm != null && timesConfirm.isCheckedAll();
    }
}
