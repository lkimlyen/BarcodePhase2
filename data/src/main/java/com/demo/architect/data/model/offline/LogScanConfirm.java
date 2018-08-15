package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirm extends RealmObject {


    @PrimaryKey
    private int id;

    @SerializedName("pOutputProductDetailID")
    @Expose
    private int productDetailID;
    private int orderId;

    private int departmentIDIn;
    private int departmentIDOut;

    private String productDetailName;
    private String module;
    private String barcode;
    private int numberTotalOrder;
    private int timesOutput;

    @SerializedName("pNumberComfirmed")
    @Expose
    private int numberConfirmed;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    @SerializedName("pTimes")
    @Expose
    private int timesInput;

    private int status;
    private int statusConfirm;
    private int numberOut;
    private String dateScan;
    private String dateConfirm;

    public LogScanConfirm() {
    }

    public LogScanConfirm(int id, int productDetailID, int orderId, int departmentIDIn, int departmentIDOut,
                          String productDetailName, String module, String barcode,
                          int numberTotalOrder, int timesOutput, int numberConfirmed,
                          int userId, int timesInput,
                          int numberOut, String dateScan, int status) {
        this.id = id;
        this.productDetailID = productDetailID;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailName = productDetailName;
        this.module = module;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.timesOutput = timesOutput;
        this.numberConfirmed = numberConfirmed;
        this.userId = userId;
        this.timesInput = timesInput;
        this.status = status;
        this.numberOut = numberOut;
        this.dateScan = dateScan;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanConfirm.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void createOrUpdate(Realm realm, OrderConfirmEntity orderConfirmEntity, int userId) {
        List<NumberInputConfirm> confirmList = orderConfirmEntity.getListInputConfirmed();
        for (NumberInputConfirm input : confirmList) {
            LogScanConfirm scanConfirm = realm.where(LogScanConfirm.class).equalTo("productDetailID", orderConfirmEntity.getOutputProductDetailID())
                    .equalTo("orderId", orderConfirmEntity.getOrderId())
                    .equalTo("departmentIDOut", orderConfirmEntity.getDepartmentIDOut())
                    .equalTo("pTimes", input.getTimesInput()).equalTo("status", Constants.WAITING_UPLOAD)
                    .equalTo("userId", userId).findFirst();
            if (scanConfirm == null) {
                scanConfirm = new LogScanConfirm(id(realm) + 1, orderConfirmEntity.getOutputProductDetailID(),
                        orderConfirmEntity.getOrderId(), orderConfirmEntity.getDepartmentIDIn(), orderConfirmEntity.getDepartmentIDOut(),
                        orderConfirmEntity.getProductDetailName(), orderConfirmEntity.getModule(), orderConfirmEntity.getBarcode(),
                        orderConfirmEntity.getNumberTotalOrder(), orderConfirmEntity.getTimesOutput(),
                        0, userId, input.getTimesInput(), orderConfirmEntity.getNumberOut(),
                        orderConfirmEntity.getDateTimeScan(), Constants.WAITING_UPLOAD);
                scanConfirm = realm.copyToRealm(scanConfirm);
            } else {
                scanConfirm.setNumberOut(orderConfirmEntity.getNumberOut());
            }
            int sum = LogScanConfirm.sumNumberScanLogWaitingUpload(realm, orderConfirmEntity.getOrderId(), orderConfirmEntity.getProductDetailID(),
                    userId, orderConfirmEntity.getDepartmentIDOut());
            if (sum > 0) {
                if (scanConfirm.getNumberOut() > sum) {
                    scanConfirm.setStatus(Constants.INCOMPLETE);
                } else if (scanConfirm.getNumberOut() == sum) {
                    scanConfirm.setStatus(Constants.FULL);
                } else {
                    scanConfirm.setStatus(Constants.RESIDUAL);
                }
            } else {
                scanConfirm.setStatus(-1);
            }

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

    public static int sumNumberScanLogWaitingUpload(Realm realm, int orderId, int productDetailID, int userId, int departmentId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("orderId", orderId)
                .equalTo("productDetailID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("departmentId", departmentId).findAll();
        if (results == null) {
            return 0;
        } else {
            int sum = results.sum("numberConfirmed").intValue();
            return sum;
        }

    }

    public static void updateNumberScan(Realm realm, int orderId, int orderProductId, int departmentIdOut, int times, int numberScan, int userId, boolean scan) {
        LogScanConfirm scanConfirm = realm.where(LogScanConfirm.class).equalTo("productDetailID", orderProductId)
                .equalTo("departmentIDOut", departmentIdOut)
                .equalTo("orderId", orderId)
                .equalTo("pTimes", times).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findFirst();
        if (scan) {
            scanConfirm.setNumberConfirmed(scanConfirm.getNumberConfirmed() + numberScan);
        } else {
            scanConfirm.setNumberConfirmed(numberScan);
        }

        int sum = LogScanConfirm.sumNumberScanLogWaitingUpload(realm, orderId, scanConfirm.getProductDetailID(),
                scanConfirm.getUserId(), scanConfirm.getDepartmentIDOut());
        if (sum > 0) {
            if (scanConfirm.getNumberOut() > sum) {
                scanConfirm.setStatus(Constants.INCOMPLETE);
            } else if (scanConfirm.getNumberOut() == sum) {
                scanConfirm.setStatus(Constants.FULL);
            } else {
                scanConfirm.setStatus(Constants.RESIDUAL);
            }
        } else {
            scanConfirm.setStatus(-1);
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

    public static RealmResults<LogScanConfirm> getListScanConfirm(Realm realm, int orderId, int departmentIDOut, int times, int userId) {
        RealmResults<LogScanConfirm> list = realm.where(LogScanConfirm.class).equalTo("orderId",orderId)
                .equalTo("departmentIDOut",departmentIDOut)
                .equalTo("userId",userId)
                .equalTo("times",times).findAll();
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }

    public int getDepartmentIDIn() {
        return departmentIDIn;
    }

    public void setDepartmentIDIn(int departmentIDIn) {
        this.departmentIDIn = departmentIDIn;
    }

    public int getDepartmentIDOut() {
        return departmentIDOut;
    }

    public void setDepartmentIDOut(int departmentIDOut) {
        this.departmentIDOut = departmentIDOut;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public void setProductDetailName(String productDetailName) {
        this.productDetailName = productDetailName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getNumberTotalOrder() {
        return numberTotalOrder;
    }

    public void setNumberTotalOrder(int numberTotalOrder) {
        this.numberTotalOrder = numberTotalOrder;
    }

    public int getTimesOutput() {
        return timesOutput;
    }

    public void setTimesOutput(int timesOutput) {
        this.timesOutput = timesOutput;
    }

    public int getNumberConfirmed() {
        return numberConfirmed;
    }

    public void setNumberConfirmed(int numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTimesInput() {
        return timesInput;
    }

    public void setTimesInput(int timesInput) {
        this.timesInput = timesInput;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusConfirm() {
        return statusConfirm;
    }

    public void setStatusConfirm(int statusConfirm) {
        this.statusConfirm = statusConfirm;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(int numberOut) {
        this.numberOut = numberOut;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public String getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(String dateConfirm) {
        this.dateConfirm = dateConfirm;
    }
}
