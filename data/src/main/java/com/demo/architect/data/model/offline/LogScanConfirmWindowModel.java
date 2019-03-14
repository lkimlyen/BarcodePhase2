package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirmWindowModel extends RealmObject {
    @PrimaryKey
    private long id;

    @SerializedName("pOutputID")
    @Expose
    private long outputId;
    private long orderId;
    private int departmentIDIn;
    private int departmentIDOut;
    private long productSetDetailId;
    private String barcode;
    private int numberTotalOrder;
    private int numberOut;
    @SerializedName("pNumberComfirmed")
    @Expose
    private int numberConfirmed;
    private long userId;
    private boolean isPrint;
    private int status;
    private int statusConfirm;
    private String dateConfirm;
    private boolean state;
    private DeliveryNoteWindowModel deliveryNoteModel;

    public LogScanConfirmWindowModel() {
    }

    public LogScanConfirmWindowModel(long id, long outputId, long orderId, int departmentIDIn,
                                     int departmentIDOut, long productSetDetailId, String barcode,
                                     int numberTotalOrder, int numberOut, int numberConfirmed,
                                     long userId, boolean isPrint, int status,
                                     String dateConfirm, boolean state) {
        this.id = id;
        this.outputId = outputId;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productSetDetailId = productSetDetailId;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.numberOut = numberOut;
        this.numberConfirmed = numberConfirmed;
        this.userId = userId;
        this.isPrint = isPrint;
        this.status = status;
        this.dateConfirm = dateConfirm;
        this.state = state;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanConfirmWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void createOrUpdate(Realm realm, OrderConfirmWindowEntity orderConfirmEntity, long userId) {
        DeliveryNoteWindowModel
            deliveryNoteModel = new DeliveryNoteWindowModel(DeliveryNoteWindowModel.id(realm) + 1, orderConfirmEntity.getOutputID(),
                    orderConfirmEntity.getProductSetID(), orderConfirmEntity.getProductSetName(),
                    orderConfirmEntity.getProductSetDetailID(), orderConfirmEntity.getProductSetDetailName(), orderConfirmEntity.getNumberOut(),
                    orderConfirmEntity.getNumberOut() - orderConfirmEntity.getNumberConfirmed(), orderConfirmEntity.getNumberConfirmed(), 0, Constants.WAITING_UPLOAD);
            deliveryNoteModel = realm.copyToRealm(deliveryNoteModel);

        LogScanConfirmWindowModel logScanConfirm = new LogScanConfirmWindowModel(id(realm) + 1, orderConfirmEntity.getOutputID(),
                orderConfirmEntity.getOrderId(), orderConfirmEntity.getDepartmentIDIn(), orderConfirmEntity.getDepartmentIDOut(),
                orderConfirmEntity.getProductSetDetailID(), orderConfirmEntity.getBarcode(),
                orderConfirmEntity.getNumberTotalOrder(), orderConfirmEntity.getNumberOut(),0,
                userId, false, Constants.WAITING_UPLOAD, DateUtils.getDateTimeCurrent(), orderConfirmEntity.isState());
        logScanConfirm.setPrint(false);
        logScanConfirm = realm.copyToRealm(logScanConfirm);
        logScanConfirm.setDeliveryNoteModel(deliveryNoteModel);
        if (logScanConfirm.getNumberConfirmed() > 0) {
            if (deliveryNoteModel.getNumberRest() > 0) {
                logScanConfirm.setStatusConfirm(Constants.INCOMPLETE);
            } else if (deliveryNoteModel.getNumberRest() == 0) {
                logScanConfirm.setStatusConfirm(Constants.FULL);
            } else {
                logScanConfirm.setStatus(Constants.RESIDUAL);
            }
        } else {
            logScanConfirm.setStatusConfirm(-1);
        }


    }


    public static void updateNumberScan(Realm realm, long outputId, int numberScan, boolean scan) {

        LogScanConfirmWindowModel scanConfirm = realm.where(LogScanConfirmWindowModel.class).equalTo("outputId", outputId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("state", false).findFirst();
        scanConfirm.setDateConfirm(DateUtils.getDateTimeCurrent());
        DeliveryNoteWindowModel deliveryNoteModel = scanConfirm.getDeliveryNoteModel();
          if (scan) {
            scanConfirm.setNumberConfirmed(scanConfirm.getNumberConfirmed() + numberScan);
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - numberScan);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + numberScan);
        } else {
            int number = numberScan - scanConfirm.getNumberConfirmed();
            scanConfirm.setNumberConfirmed(numberScan);
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - number);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + number);
        }
        if (scanConfirm.getNumberConfirmed() > 0) {
            if (deliveryNoteModel.getNumberRest() > 0) {
                scanConfirm.setStatusConfirm(Constants.INCOMPLETE);
            } else if (deliveryNoteModel.getNumberRest() == 0) {
                scanConfirm.setStatusConfirm(Constants.FULL);
            } else {
                scanConfirm.setStatusConfirm(Constants.RESIDUAL);
            }
        } else {
            scanConfirm.setStatusConfirm(-1);
        }

    }

    public static void updateStatusScanConfirm(Realm realm) {
        RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class)

                .equalTo("status", Constants.WAITING_UPLOAD).findAll();

        for (LogScanConfirmWindowModel logScanConfirm : results) {
            logScanConfirm.setStatus(Constants.COMPLETE);
            DeliveryNoteWindowModel deliveryNoteWindowModel = logScanConfirm.getDeliveryNoteModel();
            deliveryNoteWindowModel.setStatus(Constants.COMPLETE);
        }
    }


    public static LogScanConfirmWindowModel findConfirmByBarcode(Realm realm, String barcode) {
        LogScanConfirmWindowModel logScanConfirm = realm.where(LogScanConfirmWindowModel.class).equalTo("barcode", barcode)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();
        return logScanConfirm;
    }


    public static void confirmAllReceive(Realm realm) {
        RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class)
                .equalTo("state",false)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();

        for (LogScanConfirmWindowModel logScanConfirm : results) {
            DeliveryNoteWindowModel deliveryNoteModel = logScanConfirm.getDeliveryNoteModel();
            int number = (deliveryNoteModel.getNumberOut() - deliveryNoteModel.getNumberUsed()) - logScanConfirm.getNumberConfirmed();
            logScanConfirm.setNumberConfirmed(deliveryNoteModel.getNumberOut() - deliveryNoteModel.getNumberUsed());
            logScanConfirm.setStatusConfirm(Constants.FULL);
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberConfirm() + number);
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberRest() - number);
        }


    }

    public static void cancelConfirmAllReceive(Realm realm) {
        RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class)
                .equalTo("state",false)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();

        for (LogScanConfirmWindowModel logScanConfirm : results) {
            DeliveryNoteWindowModel deliveryNoteModel = logScanConfirm.getDeliveryNoteModel();
            deliveryNoteModel.setNumberConfirm(deliveryNoteModel.getNumberUsed());
            deliveryNoteModel.setNumberRest(deliveryNoteModel.getNumberOut() - deliveryNoteModel.getNumberUsed());
            logScanConfirm.setNumberConfirmed(0);
            logScanConfirm.setStatusConfirm(-1);

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

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public void setProductSetDetailId(long productSetDetailId) {
        this.productSetDetailId = productSetDetailId;
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


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isPrint() {
        return isPrint;
    }

    public void setPrint(boolean print) {
        isPrint = print;
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

    public DeliveryNoteWindowModel getDeliveryNoteModel() {
        return deliveryNoteModel;
    }

    public void setDeliveryNoteModel(DeliveryNoteWindowModel deliveryNoteModel) {
        this.deliveryNoteModel = deliveryNoteModel;
    }
}
