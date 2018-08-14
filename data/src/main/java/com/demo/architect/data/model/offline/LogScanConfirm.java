package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirm extends RealmObject {
    @PrimaryKey
    private int id;

    @SerializedName("pOutputProductDetailID")
    @Expose
    private int productDetailID;

    private int departmentId;

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

    public LogScanConfirm() {
    }

    public LogScanConfirm(int productDetailID, int departmentId, int numberComfirmed, int userId, int times, String dateScan) {

        this.productDetailID = productDetailID;
        this.departmentId = departmentId;
        this.numberComfirmed = numberComfirmed;
        this.userId = userId;
        this.times = times;
        this.dateScan = dateScan;
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
        NumberInputConfirmModel numberInputConfirm = parent.getListInputConfirmed().where().equalTo("timesInput", logScanConfirm.getTimes()).findFirst();
        LogScanConfirm scanConfirm = realm.where(LogScanConfirm.class).equalTo("productDetailID", logScanConfirm.getProductDetailID())
                .equalTo("pTimes", logScanConfirm.getTimes()).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", logScanConfirm.getUserId()).findFirst();
        if (scanConfirm == null) {
            scanConfirm = new LogScanConfirm(logScanConfirm.getProductDetailID(),
                    logScanConfirm.getDepartmentId(), logScanConfirm.getNumberComfirmed(), logScanConfirm.getUserId(), logScanConfirm.getTimes(), logScanConfirm.getDateScan());
            scanConfirm.setId(id(realm)+1);
            scanConfirm.setStatus(Constants.WAITING_UPLOAD);
            scanConfirm = realm.copyToRealm(scanConfirm);
            numberInputConfirm.setListScanConfirm(scanConfirm);
        } else {
            scanConfirm.setNumberComfirmed(logScanConfirm.getNumberComfirmed());
        }
        int sum = LogScanConfirm.sumNumberScanLogWaitingUpload(realm, logScanConfirm.getProductDetailID(),
                logScanConfirm.getUserId(), parent.getDepartmentIDOut());
        if (sum > 0) {
            if (parent.getNumberOut() > sum) {
                parent.setStatus(Constants.INCOMPLETE);
            } else if (parent.getNumberOut() == sum) {
                parent.setStatus(Constants.FULL);
            } else {
                parent.setStatus(Constants.RESIDUAL);
            }
        } else {
            parent.setStatus(-1);
        }
    }

    public static LogScanConfirm findLogWaitingUploadbyTimes(Realm realm, int productDetailID, int userId, int times, int departmentId) {
        LogScanConfirm results = realm.where(LogScanConfirm.class).equalTo("productDetailID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("times", times)
                .equalTo("departmentId", departmentId).findFirst();
        return results;

    }

    public static List<LogScanConfirm> getListLogScanConfirm(Realm realm, int userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        return realm.copyFromRealm(results);

    }

    public static int sumNumberScanLogWaitingUpload(Realm realm, int productDetailID, int userId, int departmentId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class).equalTo("productDetailID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("departmentId", departmentId).findAll();
        if (results == null) {
            return 0;
        } else {
            int sum = results.sum("numberComfirmed").intValue();
            return sum;
        }

    }

    public static void updateNumberScan(Realm realm, int logId, int numberScan) {
        LogScanConfirm results = realm.where(LogScanConfirm.class).equalTo("id", logId).findFirst();
        results.setNumberComfirmed(numberScan);
        int sum = LogScanConfirm.sumNumberScanLogWaitingUpload(realm, results.getProductDetailID(),
                results.getUserId(), results.getDepartmentId());
        ConfirmInputModel parent = realm.where(ConfirmInputModel.class).equalTo("id", results.getProductDetailID()).findFirst();
        if (sum > 0) {
            if (parent.getNumberOut() > sum) {
                parent.setStatus(Constants.INCOMPLETE);
            } else if (parent.getNumberOut() == sum) {
                parent.setStatus(Constants.FULL);
            } else {
                parent.setStatus(Constants.RESIDUAL);
            }
        } else {
            parent.setStatus(-1);
        }
    }

    public static void updateStatusScanConfirm(Realm realm, int userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        for (LogScanConfirm logScanConfirm : results) {
            logScanConfirm.setStatus(Constants.COMPLETE);
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
