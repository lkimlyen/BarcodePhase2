package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.TimesConfirm;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirmModel extends RealmObject {
    @PrimaryKey
    private long id;
    @SerializedName("pOutputID")
    @Expose
    private long outputId;
    private long orderId;
    private int departmentIDIn;
    private int departmentIDOut;
    private long productDetailId;
    private String barcode;
    private int numberTotalOrder;
    private int numberOut;
    @SerializedName("pNumberComfirmed")
    @Expose
    private int numberConfirmed;
    private int numberRestInTimes;
    private int numberUsedInTimes;
    @SerializedName("pUserID")
    @Expose
    private long userId;
    @SerializedName("pTimes")
    @Expose
    private int timesInput;
    private int status;
    private int statusConfirm;
    private String dateConfirm;
    private boolean state;
    private DeliveryNoteModel deliveryNoteModel;

    public LogScanConfirmModel() {
    }

    public LogScanConfirmModel(long id, long outputId, long orderId, int departmentIDIn, int departmentIDOut,
                               long productDetailId, String barcode,
                               int numberTotalOrder, int numberConfirmed, int numberRestInTimes,
                               int numberUsedInTimes, long userId, int timesInput,
                               int numberOut, int status, boolean state) {
        this.id = id;
        this.outputId = outputId;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailId = productDetailId;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.numberConfirmed = numberConfirmed;
        this.numberRestInTimes = numberRestInTimes;
        this.numberUsedInTimes = numberUsedInTimes;
        this.userId = userId;
        this.timesInput = timesInput;
        this.status = status;
        this.numberOut = numberOut;
        this.state = state;
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanConfirmModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void createOrUpdate(Realm realm, OrderConfirmEntity orderConfirmEntity, int times,int numberConfirmed, long userId) {

        DeliveryNoteModel deliveryNoteModel = new DeliveryNoteModel(DeliveryNoteModel.id(realm) + 1, orderConfirmEntity.getOrderId(),
                orderConfirmEntity.getOutputID(), orderConfirmEntity.getProductId(),
                orderConfirmEntity.getModule(), orderConfirmEntity.getProductDetailID(),
                orderConfirmEntity.getProductDetailName(), orderConfirmEntity.getNumberOut(),
                orderConfirmEntity.getNumberOut() - orderConfirmEntity.getNumberConfirmed(),
                orderConfirmEntity.getNumberConfirmed(), 0, Constants.WAITING_UPLOAD);
        deliveryNoteModel = realm.copyToRealm(deliveryNoteModel);

        int numberRestInTimes = Math.min(orderConfirmEntity.getNumberOut() - orderConfirmEntity.getNumberConfirmed(),
                orderConfirmEntity.getNumberTotalOrder() - numberConfirmed);
        LogScanConfirmModel logScanConfirmModel = new LogScanConfirmModel(id(realm) + 1, orderConfirmEntity.getOutputID(),
                orderConfirmEntity.getOrderId(), orderConfirmEntity.getDepartmentIDIn(),
                orderConfirmEntity.getDepartmentIDOut(), orderConfirmEntity.getProductDetailID(),
                orderConfirmEntity.getBarcode(), orderConfirmEntity.getNumberTotalOrder(),
                0, numberRestInTimes, numberConfirmed, userId, times, orderConfirmEntity.getNumberOut(),
                Constants.WAITING_UPLOAD, orderConfirmEntity.isState());
        logScanConfirmModel = realm.copyToRealm(logScanConfirmModel);
        logScanConfirmModel.setDeliveryNoteModel(deliveryNoteModel);
        if (logScanConfirmModel.getNumberConfirmed() > 0) {
            if (logScanConfirmModel.getNumberRestInTimes() > 0) {
                logScanConfirmModel.setStatusConfirm(Constants.INCOMPLETE);
            } else if (logScanConfirmModel.getNumberRestInTimes() == 0) {
                logScanConfirmModel.setStatusConfirm(Constants.FULL);
            } else {
                logScanConfirmModel.setStatus(Constants.RESIDUAL);
            }
        } else {
            logScanConfirmModel.setStatusConfirm(-1);
        }
    }


    public static List<LogScanConfirmModel> getListLogScanConfirm(Realm realm) {
        RealmResults<LogScanConfirmModel> results = realm.where(LogScanConfirmModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);

    }

    public static void updateNumberScan(Realm realm, long outputId, int numberScan, boolean scan) {
        LogScanConfirmModel scanConfirm = realm.where(LogScanConfirmModel.class).equalTo("outputId", outputId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("state", false).findFirst();
        scanConfirm.setDateConfirm(DateUtils.getDateTimeCurrent());
        DeliveryNoteModel deliveryNoteModel = scanConfirm.getDeliveryNoteModel();
        if (scan) {
            scanConfirm.setNumberConfirmed(scanConfirm.getNumberConfirmed() + numberScan);
            scanConfirm.setNumberRestInTimes(scanConfirm.getNumberRestInTimes() - numberScan);
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - numberScan);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + numberScan);
        } else {
            int number = numberScan - scanConfirm.getNumberConfirmed();
            scanConfirm.setNumberConfirmed(numberScan);
            scanConfirm.setNumberRestInTimes(scanConfirm.getNumberRestInTimes() - number);
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - number);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + number);
        }
        if (scanConfirm.getNumberConfirmed() > 0) {
            if (scanConfirm.getNumberRestInTimes() > 0) {
                scanConfirm.setStatusConfirm(Constants.INCOMPLETE);
            } else if (scanConfirm.getNumberRestInTimes() == 0) {
                scanConfirm.setStatusConfirm(Constants.FULL);
            } else {
                scanConfirm.setStatusConfirm(Constants.RESIDUAL);
            }
        } else {
            scanConfirm.setStatusConfirm(-1);
        }

    }

    public static void updateStatusScanConfirm(Realm realm) {
        RealmResults<LogScanConfirmModel> results = realm.where(LogScanConfirmModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();
        for (LogScanConfirmModel logScanConfirmModel : results) {
            logScanConfirmModel.setStatus(Constants.COMPLETE);
            DeliveryNoteModel deliveryNoteModel = logScanConfirmModel.getDeliveryNoteModel();
            deliveryNoteModel.setStatus(Constants.COMPLETE);
        }
    }

    public static RealmResults<LogScanConfirmModel> getListScanConfirm(Realm realm) {
        final RealmResults<LogScanConfirmModel> list = realm.where(LogScanConfirmModel.class)
                .equalTo("state", false)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();
        return list;
    }

    public static LogScanConfirmModel findConfirmByBarcode(Realm realm, String barcode) {
        LogScanConfirmModel logScanConfirmModel = realm.where(LogScanConfirmModel.class).equalTo("barcode", barcode)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();
        return logScanConfirmModel;
    }


    public static void confirmAllReceive(Realm realm) {
        RealmResults<LogScanConfirmModel> results = realm.where(LogScanConfirmModel.class)
                .equalTo("state", false).equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();

        for (LogScanConfirmModel logScanConfirmModel : results) {
            DeliveryNoteModel deliveryNoteModel = logScanConfirmModel.getDeliveryNoteModel();
            logScanConfirmModel.setNumberConfirmed(logScanConfirmModel.getNumberRestInTimes() + logScanConfirmModel.getNumberConfirmed());
            logScanConfirmModel.setNumberRestInTimes(0);
            logScanConfirmModel.setStatusConfirm(Constants.FULL);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + logScanConfirmModel.getNumberConfirmed());
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - logScanConfirmModel.getNumberConfirmed());
        }
    }

    public static void cancelConfirmAllReceive(Realm realm) {
        RealmResults<LogScanConfirmModel> results = realm.where(LogScanConfirmModel.class)
                .equalTo("state", false)
                .equalTo("status", Constants.WAITING_UPLOAD)
            .findAll();

        for (LogScanConfirmModel logScanConfirmModel : results) {
            DeliveryNoteModel deliveryNoteModel = logScanConfirmModel.getDeliveryNoteModel();
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + logScanConfirmModel.getNumberConfirmed());
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - logScanConfirmModel.getNumberConfirmed());
            logScanConfirmModel.setNumberConfirmed(0);
            logScanConfirmModel.setNumberRestInTimes(Math.min(deliveryNoteModel.getNumberOut() - deliveryNoteModel.getNumberUsed(),
                    logScanConfirmModel.getNumberTotalOrder() - logScanConfirmModel.getNumberUsedInTimes()));
            if (logScanConfirmModel.getNumberConfirmed() > 0) {
                if (logScanConfirmModel.getNumberRestInTimes() > 0) {
                    logScanConfirmModel.setStatusConfirm(Constants.INCOMPLETE);
                } else {
                    logScanConfirmModel.setStatusConfirm(Constants.FULL);
                }
            } else {
                logScanConfirmModel.setStatusConfirm(-1);
            }

        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOutputId() {
        return outputId;
    }

    public void setOutputId(long outputId) {
        this.outputId = outputId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
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

    public int getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(int numberOut) {
        this.numberOut = numberOut;
    }

    public int getNumberConfirmed() {
        return numberConfirmed;
    }

    public void setNumberConfirmed(int numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
    }

    public int getNumberRestInTimes() {
        return numberRestInTimes;
    }

    public void setNumberRestInTimes(int numberRestInTimes) {
        this.numberRestInTimes = numberRestInTimes;
    }

    public int getNumberUsedInTimes() {
        return numberUsedInTimes;
    }

    public void setNumberUsedInTimes(int numberUsedInTimes) {
        this.numberUsedInTimes = numberUsedInTimes;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public String getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(String dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public DeliveryNoteModel getDeliveryNoteModel() {
        return deliveryNoteModel;
    }

    public void setDeliveryNoteModel(DeliveryNoteModel deliveryNoteModel) {
        this.deliveryNoteModel = deliveryNoteModel;
    }
}
