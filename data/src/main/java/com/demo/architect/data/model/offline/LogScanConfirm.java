package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.utils.view.DateUtils;
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

    @SerializedName("pMasterOutputID")
    @Expose
    private int masterOutputID;
    private int orderId;
    private int departmentIDIn;
    private int departmentIDOut;
    private String productDetailName;
    private String module;
    private String barcode;
    private int numberTotalOrder;

    @SerializedName("pNumberComfirmed")
    @Expose
    private int numberConfirmed;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    @SerializedName("pTimes")
    @Expose
    private int timesInput;

    private RealmList<NumberInputConfirmModel> list;
    private int status;
    private int statusConfirm;
    private int numberOut;
    private int numberScanOut;
    private String dateConfirm;
    @SerializedName("pLastTimeGetData")
    @Expose
    private String lastTimeGetData;

    public LogScanConfirm() {
    }

    public LogScanConfirm(int id, int masterOutputID, int orderId, int departmentIDIn, int departmentIDOut,
                          String productDetailName, String module, String barcode,
                          int numberTotalOrder, int numberConfirmed,
                          int userId, int timesInput, int numberOut, int status, String lastTimeGetData) {
        this.id = id;
        this.masterOutputID = masterOutputID;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailName = productDetailName;
        this.module = module;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.numberConfirmed = numberConfirmed;
        this.userId = userId;
        this.timesInput = timesInput;
        this.status = status;
        this.numberOut = numberOut;
        this.lastTimeGetData = lastTimeGetData;
    }

    public LogScanConfirm(int id, int masterOutputID, int orderId, int departmentIDIn,
                          int departmentIDOut, String productDetailName, String module,
                          String barcode, int numberTotalOrder, int numberConfirmed,
                          int userId, int timesInput, int status, int statusConfirm,
                          int numberOut, int numberScanOut, String dateConfirm, String lastTimeGetData) {
        this.id = id;
        this.masterOutputID = masterOutputID;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailName = productDetailName;
        this.module = module;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.numberConfirmed = numberConfirmed;
        this.userId = userId;
        this.timesInput = timesInput;
        this.status = status;
        this.statusConfirm = statusConfirm;
        this.numberOut = numberOut;
        this.numberScanOut = numberScanOut;
        this.dateConfirm = dateConfirm;
        this.lastTimeGetData = lastTimeGetData;
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
        RealmList<NumberInputConfirmModel> results = NumberInputConfirmModel.createOrUpdate(realm, confirmList, orderConfirmEntity.getMasterOutputID(), orderConfirmEntity.getNumberOut());
        for (NumberInputConfirm input : confirmList) {
            LogScanConfirm logScanConfirm = realm.where(LogScanConfirm.class)
                    .equalTo("masterOutputID", orderConfirmEntity.getMasterOutputID())
                    .equalTo("orderId", orderConfirmEntity.getOrderId())
                    .equalTo("departmentIDOut", orderConfirmEntity.getDepartmentIDOut())
                    .equalTo("timesInput", input.getTimesInput()).equalTo("status", Constants.WAITING_UPLOAD)
                    .equalTo("userId", userId).findFirst();
            if (logScanConfirm == null) {
                logScanConfirm = new LogScanConfirm(id(realm) + 1, orderConfirmEntity.getMasterOutputID(),
                        orderConfirmEntity.getOrderId(), orderConfirmEntity.getDepartmentIDIn(), orderConfirmEntity.getDepartmentIDOut(),
                        orderConfirmEntity.getProductDetailName(), orderConfirmEntity.getModule(), orderConfirmEntity.getBarcode(),
                        orderConfirmEntity.getNumberTotalOrder(),
                        0, userId, input.getTimesInput(), orderConfirmEntity.getNumberOut(),
                        Constants.WAITING_UPLOAD, orderConfirmEntity.getLastTimeGetData());
                logScanConfirm = realm.copyToRealm(logScanConfirm);
                RealmList<NumberInputConfirmModel> parentList = logScanConfirm.getList();
                parentList.clear();
                parentList.addAll(results);

            } else {
                logScanConfirm.setNumberOut(orderConfirmEntity.getNumberOut());
                RealmList<NumberInputConfirmModel> parentList = logScanConfirm.getList();
                parentList.clear();
                parentList.addAll(results);
                logScanConfirm.setLastTimeGetData(orderConfirmEntity.getLastTimeGetData());

            }
            RealmList<NumberInputConfirmModel> parentList = logScanConfirm.getList();
            NumberInputConfirmModel numberInputConfirm = parentList.where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
            logScanConfirm.setNumberScanOut(numberInputConfirm.getNumberScanOut());
            if (logScanConfirm.getNumberConfirmed() > 0) {
                if (logScanConfirm.getNumberScanOut() > logScanConfirm.getNumberConfirmed()) {
                    logScanConfirm.setStatusConfirm(Constants.INCOMPLETE);
                } else if (logScanConfirm.getNumberScanOut() == logScanConfirm.getNumberConfirmed()) {
                    logScanConfirm.setStatusConfirm(Constants.FULL);
                } else {
                    logScanConfirm.setStatusConfirm(Constants.RESIDUAL);
                }
            } else {
                logScanConfirm.setStatusConfirm(-1);
                logScanConfirm.setNumberScanOut(logScanConfirm.getNumberOut());
            }
        }
    }

    public static LogScanConfirm findLogWaitingUploadbyTimes(Realm realm, int productDetailID, int userId, int times, int departmentId) {
        LogScanConfirm results = realm.where(LogScanConfirm.class).equalTo("masterOutputID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("timesInput", times)
                .equalTo("departmentIDOut", departmentId).findFirst();
        return results;
    }

    public static List<LogScanConfirm> getListLogScanConfirm(Realm realm, int userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .notEqualTo("statusConfirm", -1)
                .equalTo("userId", userId).findAll();
        return realm.copyFromRealm(results);

    }

    public static void updateStatusConfirm(Realm realm, int orderId, int productDetailID, int userId, int departmentId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("orderId", orderId)
                .equalTo("masterOutputID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("departmentIDOut", departmentId).findAll();
        int sum = results.sum("numberConfirmed").intValue();
        for (LogScanConfirm logScanConfirm : results) {
            if (sum > 0 && logScanConfirm.getNumberConfirmed() > 0) {
                if (logScanConfirm.getNumberOut() > sum) {
                    logScanConfirm.setStatusConfirm(Constants.INCOMPLETE);
                } else if (logScanConfirm.getNumberOut() == sum) {
                    logScanConfirm.setStatusConfirm(Constants.FULL);
                } else {
                    logScanConfirm.setStatusConfirm(Constants.RESIDUAL);
                }
            } else {
                logScanConfirm.setStatusConfirm(-1);
                logScanConfirm.setNumberScanOut(logScanConfirm.getNumberOut());
            }
        }
    }

    public static int sumNumberScan(Realm realm, int orderId, int productDetailID, int userId, int departmentId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("orderId", orderId)
                .equalTo("masterOutputID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("departmentIDOut", departmentId).findAll();
        int sum = results.sum("numberConfirmed").intValue();
        return sum;
    }

    public static void updateNumberScan(Realm realm, int orderId, int masterOutputId, int departmentIdOut, int times, int numberScan, int userId, boolean scan) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class).equalTo("masterOutputID", masterOutputId)
                .equalTo("departmentIDOut", departmentIdOut)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        LogScanConfirm scanConfirm = realm.where(LogScanConfirm.class).equalTo("masterOutputID", masterOutputId)
                .equalTo("departmentIDOut", departmentIdOut)
                .equalTo("orderId", orderId)
                .equalTo("timesInput", times).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findFirst();
        scanConfirm.setDateConfirm(DateUtils. getDateTimeCurrent());
        RealmList<NumberInputConfirmModel> realmList = scanConfirm.getList();
        if (scan) {
            scanConfirm.setNumberConfirmed(scanConfirm.getNumberConfirmed() + numberScan);

            if (scanConfirm.getNumberConfirmed() <= scanConfirm.getNumberScanOut()) {
                for (NumberInputConfirmModel numberInputConfirmModel : realmList) {
                    if (numberInputConfirmModel.getTimesInput() == times) {
                        numberInputConfirmModel.setNumberConfirmed(scanConfirm.getNumberConfirmed());
                    } else {
                        numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() - 1);
                    }
                }
                for (LogScanConfirm logScanConfirm : results) {
                    NumberInputConfirmModel numberInputConfirmModel = realmList.where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
                    logScanConfirm.setNumberScanOut(numberInputConfirmModel.getNumberScanOut());
                    if (logScanConfirm.getNumberConfirmed() > 0) {
                        if (logScanConfirm.getNumberScanOut() > logScanConfirm.getNumberConfirmed()) {
                            logScanConfirm.setStatusConfirm(Constants.INCOMPLETE);
                        } else if (logScanConfirm.getNumberScanOut() == logScanConfirm.getNumberConfirmed()) {
                            logScanConfirm.setStatusConfirm(Constants.FULL);
                        } else {
                            logScanConfirm.setStatusConfirm(Constants.RESIDUAL);
                        }
                    } else {
                        logScanConfirm.setStatusConfirm(-1);
                        logScanConfirm.setNumberScanOut(logScanConfirm.getNumberOut());
                    }
                }

            }

        } else {
            int numberScanRest = scanConfirm.getNumberScanOut() - scanConfirm.getNumberConfirmed();
            int numberScanCurrent = numberScan - scanConfirm.getNumberConfirmed();
            scanConfirm.setNumberConfirmed(numberScan);
            if (numberScanCurrent <= numberScanRest) {
                for (NumberInputConfirmModel numberInputConfirmModel : realmList) {
                    if (numberInputConfirmModel.getTimesInput() == times) {
                        numberInputConfirmModel.setNumberConfirmed(scanConfirm.getNumberConfirmed());
                    } else {
                        numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() - numberScanCurrent);
                    }
                }
            } else {
                for (NumberInputConfirmModel numberInputConfirmModel : realmList) {
                    if (numberInputConfirmModel.getTimesInput() == times) {
                        numberInputConfirmModel.setNumberConfirmed(scanConfirm.getNumberConfirmed());
                    } else {
                        numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() - numberScanRest);
                    }
                }
            }

        }


        for (LogScanConfirm logScanConfirm : results) {
            NumberInputConfirmModel numberInputConfirmModel = realmList.where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
            logScanConfirm.setNumberScanOut(numberInputConfirmModel.getNumberScanOut());
            if (logScanConfirm.getNumberConfirmed() > 0) {
                if (logScanConfirm.getNumberScanOut() > logScanConfirm.getNumberConfirmed()) {
                    logScanConfirm.setStatusConfirm(Constants.INCOMPLETE);
                } else if (logScanConfirm.getNumberScanOut() == logScanConfirm.getNumberConfirmed()) {
                    logScanConfirm.setStatusConfirm(Constants.FULL);
                } else {
                    logScanConfirm.setStatusConfirm(Constants.RESIDUAL);
                }
            } else {
                logScanConfirm.setStatusConfirm(-1);
            }
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
        final RealmResults<LogScanConfirm> list = realm.where(LogScanConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .notEqualTo("numberScanOut", 0)
                .equalTo("timesInput", times).findAll();
        return list;
    }

    public static int countListConfirmByTimesWaitingUpload(Realm realm, int orderId, int departmentIDOut, int times, int userId) {
        final RealmResults<LogScanConfirm> list = realm.where(LogScanConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .notEqualTo("statusConfirm", -1)
                .equalTo("timesInput", times).findAll();
        return list.size();
    }

    public static LogScanConfirm findConfirmByBarcode(Realm realm, String barcode, int orderId,
                                                      int departmentIDOut, int times, int userId) {
        LogScanConfirm logScanConfirm = realm.where(LogScanConfirm.class).equalTo("barcode", barcode)
                .equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("timesInput", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();
        return logScanConfirm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterOutputID() {
        return masterOutputID;
    }

    public void setMasterOutputID(int masterOutputID) {
        this.masterOutputID = masterOutputID;
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

    public void setDateScan(String dateScan) {
    }

    public String getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(String dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getNumberScanOut() {
        return numberScanOut;
    }

    public void setNumberScanOut(int numberScanOut) {
        this.numberScanOut = numberScanOut;
    }

    public RealmList<NumberInputConfirmModel> getList() {
        return list;
    }

    public void setList(RealmList<NumberInputConfirmModel> list) {
        this.list = list;
    }

    public String getLastTimeGetData() {
        return lastTimeGetData;
    }

    public void setLastTimeGetData(String lastTimeGetData) {
        this.lastTimeGetData = lastTimeGetData;
    }
}
