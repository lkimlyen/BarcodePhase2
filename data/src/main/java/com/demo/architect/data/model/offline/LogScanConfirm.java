package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.TimesConfirm;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanConfirm extends RealmObject {


    @PrimaryKey
    private long id;

    @SerializedName("pMasterOutputID")
    @Expose
    private long masterOutputID;
    private long orderId;

    public long getProductDetailId() {
        return productDetailId;
    }

    public double getNumberConfirmedAll() {
        return numberConfirmedAll;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public long getLastIDOutput() {
        return lastIDOutput;
    }

    public void setLastIDOutput(long lastIDOutput) {
        this.lastIDOutput = lastIDOutput;
    }

    private int departmentIDIn;
    private int departmentIDOut;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMasterOutputID() {
        return masterOutputID;
    }

    public void setMasterOutputID(long masterOutputID) {
        this.masterOutputID = masterOutputID;
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

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
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

    public double getNumberTotalOrder() {
        return numberTotalOrder;
    }

    public void setNumberTotalOrder(double numberTotalOrder) {
        this.numberTotalOrder = numberTotalOrder;
    }

    public double getNumberConfirmed() {
        return numberConfirmed;
    }

    public void setNumberConfirmed(double numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
    }

    public void setNumberConfirmedAll(double numberConfirmedAll) {
        this.numberConfirmedAll = numberConfirmedAll;
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

    public double getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(double numberOut) {
        this.numberOut = numberOut;
    }

    public double getNumberScanOut() {
        return numberScanOut;
    }

    public void setNumberScanOut(double numberScanOut) {
        this.numberScanOut = numberScanOut;
    }

    public String getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(String dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    private long productDetailId;
    private String productDetailName;
    private String module;
    private String barcode;
    private double numberTotalOrder;

    @SerializedName("pNumberComfirmed")
    @Expose
    private double numberConfirmed;

    private double numberConfirmedAll;
    private boolean all;
    @SerializedName("pUserID")
    @Expose
    private long userId;

    @SerializedName("pTimes")
    @Expose
    private int timesInput;

    private RealmList<NumberInputConfirmModel> list;
    private int status;
    private int statusConfirm;
    private double numberOut;
    private double numberScanOut;
    private String dateConfirm;
    @SerializedName("pLastIDOutput")
    @Expose
    private long lastIDOutput;


    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @SerializedName("pState")
    @Expose
    private boolean state;

    public LogScanConfirm() {
    }

    public LogScanConfirm(long id, long masterOutputID, long orderId, int departmentIDIn, int departmentIDOut,
                          long productDetailId, String productDetailName, String module, String barcode,
                          double numberTotalOrder, double numberConfirmed,
                          double numberConfirmedAll, boolean all, long userId, int timesInput, double numberOut, int status, long lastIDOutput, boolean state) {
        this.id = id;
        this.masterOutputID = masterOutputID;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailId = productDetailId;
        this.productDetailName = productDetailName;
        this.module = module;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.numberConfirmed = numberConfirmed;
        this.numberConfirmedAll = numberConfirmedAll;
        this.userId = userId;
        this.timesInput = timesInput;
        this.status = status;
        this.numberOut = numberOut;
        this.all = all;
        this.lastIDOutput = lastIDOutput;
        this.state = state;
    }


    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(LogScanConfirm.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void createOrUpdate(Realm realm, OrderConfirmEntity orderConfirmEntity, long userId) {
        List<NumberInputConfirm> confirmList = orderConfirmEntity.getListInputConfirmed();
        RealmList<NumberInputConfirmModel> list = new RealmList<>();
        RealmList<LogScanConfirm> logScanConfirms = new RealmList<>();
        for (NumberInputConfirm input : confirmList) {
            if (input.getNumberConfirmed() < orderConfirmEntity.getNumberTotalOrder()) {
                LogScanConfirm logScanConfirm = realm.where(LogScanConfirm.class)
                        .equalTo("masterOutputID", orderConfirmEntity.getMasterOutputID())
                        .equalTo("orderId", orderConfirmEntity.getOrderId())
                        .equalTo("departmentIDOut", orderConfirmEntity.getDepartmentIDOut())
                        .equalTo("timesInput", input.getTimesInput()).equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("userId", userId).findFirst();
                if (logScanConfirm == null) {
                    logScanConfirm = new LogScanConfirm(id(realm) + 1, orderConfirmEntity.getMasterOutputID(),
                            orderConfirmEntity.getOrderId(), orderConfirmEntity.getDepartmentIDIn(), orderConfirmEntity.getDepartmentIDOut(),
                            orderConfirmEntity.getProductDetailID(), orderConfirmEntity.getProductDetailName(), orderConfirmEntity.getModule(), orderConfirmEntity.getBarcode(),
                            orderConfirmEntity.getNumberTotalOrder(),
                            0, 0, false, userId, input.getTimesInput(), orderConfirmEntity.getNumberOut(),
                            Constants.WAITING_UPLOAD, orderConfirmEntity.getLastIDOutput(), orderConfirmEntity.isState());
                    logScanConfirm = realm.copyToRealm(logScanConfirm);

                } else {
                    logScanConfirm.setNumberOut(orderConfirmEntity.getNumberOut());
                    logScanConfirm.setLastIDOutput(orderConfirmEntity.getLastIDOutput());
                    logScanConfirm.setState(orderConfirmEntity.isState());
                }
                RealmList<NumberInputConfirmModel> parentList = logScanConfirm.getList();
                NumberInputConfirmModel numberInputConfirm = parentList.where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
                double sum = sumNumberScan(realm, orderConfirmEntity.getOrderId(), orderConfirmEntity.getMasterOutputID(), userId, orderConfirmEntity.getDepartmentIDOut());
                double numberRest = orderConfirmEntity.getNumberOut() - sum;
                if (numberInputConfirm == null) {
                    numberInputConfirm = NumberInputConfirmModel.create(realm, input, logScanConfirm.masterOutputID, orderConfirmEntity.getNumberOut(), orderConfirmEntity.getNumberTotalOrder());
                    parentList.add(numberInputConfirm);
                } else {
                    numberInputConfirm.setNumberOut(orderConfirmEntity.getNumberOut());

                    if (numberRest > 0) {
                        if (numberInputConfirm.getNumberScanOut() < numberRest && numberRest <= orderConfirmEntity.getNumberTotalOrder() - input.getNumberConfirmed()) {
                            numberInputConfirm.setNumberScanOut(numberRest);
                        } else if (numberRest > orderConfirmEntity.getNumberTotalOrder() - input.getNumberConfirmed()) {
                            numberInputConfirm.setNumberScanOut(orderConfirmEntity.getNumberTotalOrder() - input.getNumberConfirmed());
                        }
                    }
                }

                list.add(numberInputConfirm);
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
                }
                logScanConfirms.add(logScanConfirm);
            }

            for (LogScanConfirm logScanConfirm : logScanConfirms) {
                RealmList<NumberInputConfirmModel> parentList = logScanConfirm.getList();
                parentList.clear();
                parentList.addAll(list);
            }
        }
    }


    public static List<LogScanConfirm> getListLogScanConfirm(Realm realm, long userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .notEqualTo("statusConfirm", -1)
                .greaterThan("numberConfirmed",(double) 0)
                .equalTo("userId", userId).findAll();
        return realm.copyFromRealm(results);

    }

    public static double sumNumberScan(Realm realm, long orderId, long productDetailID, long userId, int departmentId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("orderId", orderId)
                .equalTo("masterOutputID", productDetailID)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("departmentIDOut", departmentId).findAll();
        double sum = results.sum("numberConfirmed").doubleValue();
        return sum;
    }

    public static void updateNumberScan(Realm realm, long orderId, long masterOutputId, int departmentIdOut, int times, double numberScan, long userId, boolean scan) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class).equalTo("masterOutputID", masterOutputId)
                .equalTo("departmentIDOut", departmentIdOut)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        LogScanConfirm scanConfirm = realm.where(LogScanConfirm.class).equalTo("masterOutputID", masterOutputId)
                .equalTo("departmentIDOut", departmentIdOut)
                .equalTo("orderId", orderId)
                .equalTo("timesInput", times).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findFirst();
        scanConfirm.setDateConfirm(DateUtils.getDateTimeCurrent());
        RealmList<NumberInputConfirmModel> realmList = scanConfirm.getList();
        if (scan) {
            scanConfirm.setNumberConfirmed(scanConfirm.getNumberConfirmed() + numberScan);

            scanConfirm.setAll(false);
            if (scanConfirm.getNumberConfirmed() <= scanConfirm.getNumberScanOut()) {
                for (NumberInputConfirmModel numberInputConfirmModel : realmList) {
                    if (numberInputConfirmModel.getTimesInput() == times) {
                        numberInputConfirmModel.setNumberInput(scanConfirm.getNumberConfirmed());
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
            scanConfirm.setNumberConfirmed(numberScan);
            scanConfirm.setNumberConfirmedAll(scanConfirm.getNumberConfirmed());
            double sum = results.sum("numberConfirmed").doubleValue();
            double numberRest = scanConfirm.getNumberOut() - sum;
            for (NumberInputConfirmModel numberInputConfirmModel : realmList) {
                if (numberInputConfirmModel.getTimesInput() == times) {
                    numberInputConfirmModel.setNumberInput(scanConfirm.getNumberConfirmed());
                } else {
                    double numberRestInModel = numberInputConfirmModel.getNumberScanOut() - numberInputConfirmModel.getNumberInput();
                    if (numberRestInModel < numberRest) {
                        if (numberInputConfirmModel.getNumberScanOut() + numberRest <= scanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed()) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() + numberRest);
                        } else {
                            numberInputConfirmModel.setNumberScanOut(scanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed());
                        }
                    } else if (numberRestInModel > numberRest) {
                        if (numberRest == 0) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberInput());
                        } else {
                            numberInputConfirmModel.setNumberScanOut(numberRest);
                        }

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

    public static void updateStatusScanConfirm(Realm realm, long userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        for (LogScanConfirm logScanConfirm : results) {
            logScanConfirm.setStatus(Constants.COMPLETE);
            TimesConfirm timesConfirm = realm.where(TimesConfirm.class).equalTo("orderId", logScanConfirm.getOrderId())
                    .equalTo("departmentIdOut", logScanConfirm.getDepartmentIDOut())
                    .equalTo("times", logScanConfirm.getTimesInput())
                    .equalTo("userId", userId)
                    .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
            if (timesConfirm != null){
            timesConfirm.setStatus(Constants.COMPLETE);}
        }
    }

    public static RealmResults<LogScanConfirm> getListScanConfirm(Realm realm, long orderId, int departmentIDOut, int times, long userId) {
        final RealmResults<LogScanConfirm> list = realm.where(LogScanConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .greaterThan("numberScanOut", (double) 0)
                .equalTo("timesInput", times).findAll();
        return list;
    }

    public static int countListConfirmByTimesWaitingUpload(Realm realm, long orderId, int departmentIDOut, int times, long userId) {
        final RealmResults<LogScanConfirm> list = realm.where(LogScanConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .notEqualTo("statusConfirm", -1)
                .equalTo("timesInput", times).findAll();
        return list.size();
    }

    public static LogScanConfirm findConfirmByBarcode(Realm realm, String barcode, long orderId,
                                                      int departmentIDOut, int times, long userId) {
        LogScanConfirm logScanConfirm = realm.where(LogScanConfirm.class).equalTo("barcode", barcode)
                .equalTo("orderId", orderId)
                .equalTo("departmentIDOut", departmentIDOut)
                .equalTo("timesInput", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();
        return logScanConfirm;
    }

    public RealmList<NumberInputConfirmModel> getList() {
        return list;
    }

    public void setList(RealmList<NumberInputConfirmModel> list) {
        this.list = list;
    }

    public static void confirmAllReceive(Realm realm, long orderId, int departmentId, int times, long userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("departmentIDOut", departmentId)
                .equalTo("timesInput", times)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();

        RealmResults<LogScanConfirm> resultAll = realm.where(LogScanConfirm.class)
                .equalTo("departmentIDOut", departmentId)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        TimesConfirm timesConfirm = realm.where(TimesConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIdOut", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (timesConfirm == null) {
            timesConfirm = new TimesConfirm(TimesConfirm.id(realm) + 1, orderId, departmentId, times, true, userId, Constants.WAITING_UPLOAD);
            realm.copyToRealm(timesConfirm);
        } else {
            timesConfirm.setCheckedAll(true);
        }
        for (LogScanConfirm logScanConfirm : results) {

            logScanConfirm.setNumberConfirmedAll(logScanConfirm.getNumberConfirmed());
            logScanConfirm.setNumberConfirmed(logScanConfirm.getNumberScanOut());
            double sum = sumNumberScan(realm, orderId, logScanConfirm.masterOutputID, userId, departmentId);
            double numberRest = logScanConfirm.getNumberOut() - sum;
            logScanConfirm.setAll(true);
            for (NumberInputConfirmModel numberInputConfirmModel : logScanConfirm.getList()) {
                if (numberInputConfirmModel.getTimesInput() == times) {
                    numberInputConfirmModel.setNumberInput(logScanConfirm.getNumberConfirmed());
                } else {
                    double numberRestInModel = numberInputConfirmModel.getNumberScanOut() - numberInputConfirmModel.getNumberInput();
                    if (numberRestInModel < numberRest) {
                        if (numberInputConfirmModel.getNumberScanOut() + numberRest <= logScanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed()) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() + numberRest);
                        } else {
                            numberInputConfirmModel.setNumberScanOut(logScanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed());
                        }
                    } else if (numberRestInModel > numberRest) {
                        if (numberRest == 0) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberInput());
                        } else {
                            numberInputConfirmModel.setNumberScanOut(numberRest);
                        }

                    }
                }

            }
        }

        for (LogScanConfirm logScanConfirm : resultAll) {
            NumberInputConfirmModel numberInputConfirmModel = logScanConfirm.getList().where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
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

    public static void cancelConfirmAllReceive(Realm realm, long orderId, int departmentId, int times, long userId) {
        RealmResults<LogScanConfirm> results = realm.where(LogScanConfirm.class)
                .equalTo("departmentIDOut", departmentId)
                .equalTo("timesInput", times)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();

        RealmResults<LogScanConfirm> resultAll = realm.where(LogScanConfirm.class)
                .equalTo("departmentIDOut", departmentId)
                .equalTo("orderId", orderId).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findAll();
        TimesConfirm timesConfirm = realm.where(TimesConfirm.class).equalTo("orderId", orderId)
                .equalTo("departmentIdOut", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (timesConfirm == null) {
            timesConfirm = new TimesConfirm(TimesConfirm.id(realm) + 1, orderId, departmentId, times, false, userId, Constants.WAITING_UPLOAD);
            realm.copyToRealm(timesConfirm);
        } else {
            timesConfirm.setCheckedAll(false);
        }
        for (LogScanConfirm logScanConfirm : results) {

            logScanConfirm.setNumberConfirmed(logScanConfirm.getNumberConfirmedAll());
            logScanConfirm.setNumberConfirmedAll(0);
            logScanConfirm.setAll(false);
            double sum = sumNumberScan(realm, orderId, logScanConfirm.masterOutputID, userId, departmentId);
            double numberRest = logScanConfirm.getNumberOut() - sum;
            for (NumberInputConfirmModel numberInputConfirmModel : logScanConfirm.getList()) {
                if (numberInputConfirmModel.getTimesInput() == times) {
                    numberInputConfirmModel.setNumberInput(logScanConfirm.getNumberConfirmed());
                } else {
                    double numberRestInModel = numberInputConfirmModel.getNumberScanOut() - numberInputConfirmModel.getNumberInput();
                    if (numberRestInModel < numberRest) {
                        if (numberInputConfirmModel.getNumberScanOut() + numberRest <= logScanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed()) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut() + numberRest);
                        } else {
                            numberInputConfirmModel.setNumberScanOut(logScanConfirm.getNumberTotalOrder() - numberInputConfirmModel.getNumberConfirmed());
                        }
                    } else if (numberRestInModel > numberRest) {
                        if (numberRest == 0) {
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberInput());
                        } else {
                            numberInputConfirmModel.setNumberScanOut(numberRest);
                        }

                    }
                }
            }
        }

        for (LogScanConfirm logScanConfirm : resultAll) {
            NumberInputConfirmModel numberInputConfirmModel = logScanConfirm.getList().where().equalTo("timesInput", logScanConfirm.getTimesInput()).findFirst();
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


}
