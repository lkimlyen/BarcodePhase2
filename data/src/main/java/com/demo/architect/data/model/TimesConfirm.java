package com.demo.architect.data.model;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimesConfirm extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private int departmentIdOut;
    private int times;
    private boolean checkedAll;
    private long userId;
    private int status;

    public TimesConfirm() {
    }

    public TimesConfirm(long id, long orderId, int departmentIdOut, int times, boolean checkedAll, long userId, int status) {
        this.id = id;
        this.orderId = orderId;
        this.departmentIdOut = departmentIdOut;
        this.times = times;
        this.checkedAll = checkedAll;
        this.userId = userId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(TimesConfirm.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static boolean getCheckedConfirmAll(Realm realm, long orderId, int departmentId, int times, long userId) {
        TimesConfirm timesConfirm = realm.where(TimesConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIdOut", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return timesConfirm != null && timesConfirm.isCheckedAll();
    }
}
