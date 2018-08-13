package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirm extends RealmObject {
    @PrimaryKey
    private int id;

    @SerializedName("pOutputProductDetailID")
    @Expose
    private int productDetailID;

    @SerializedName("pNumberComfirmed")
    @Expose
    private int numberComfirmed;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    @SerializedName("pTimes")
    @Expose
    private int times;

    private int status;

    private String dateScan;

    public LogScanConfirm(String dateScan) {
        this.dateScan = dateScan;
    }

    public LogScanConfirm(int productDetailID, int numberComfirmed, int userId, int times) {

        this.productDetailID = productDetailID;
        this.numberComfirmed = numberComfirmed;
        this.userId = userId;
        this.times = times;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanConfirm.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, LogScanConfirm logScanConfirm) {
        ConfirmInputModel parent = realm.where(ConfirmInputModel.class).equalTo("id", logScanConfirm.getProductDetailID()).findFirst();
        RealmList<LogScanConfirm> parentList = parent.getListScanConfirm();
        LogScanConfirm numberInputModel = realm.where(LogScanConfirm.class).equalTo("productDetailID", logScanConfirm.getProductDetailID())
                .equalTo("pTimes", logScanConfirm.getTimes()).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", logScanConfirm.getUserId()).findFirst();
        if (numberInputModel == null) {
            numberInputModel = new LogScanConfirm(logScanConfirm.getProductDetailID(),
                    logScanConfirm.getNumberComfirmed(), logScanConfirm.getUserId(), logScanConfirm.getTimes());
            numberInputModel.setId(id(realm));
            numberInputModel.setStatus(Constants.WAITING_UPLOAD);
            numberInputModel = realm.copyToRealm(numberInputModel);
            parentList.add(numberInputModel);
        } else {
            numberInputModel.setNumberComfirmed(logScanConfirm.getNumberComfirmed());
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }

    public void setNumberComfirmed(int numberComfirmed) {
        this.numberComfirmed = numberComfirmed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public int getNumberComfirmed() {
        return numberComfirmed;
    }

    public int getUserId() {
        return userId;
    }

    public int getTimes() {
        return times;
    }

    public int getStatus() {
        return status;
    }

    public String getDateScan() {
        return dateScan;
    }
}
