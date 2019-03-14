package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanStagesWindowModel extends RealmObject {

    @PrimaryKey
    private long id;
    private long orderId;
    private int departmentIdIn;
    private int departmentIdOut;
    private int staffId;

    @Expose
    @SerializedName("pProductSetDetailID")
    private long productSetDetailID;

    private ProductDetailWindowModel productDetail;

    @Expose
    @SerializedName("pBarcode")
    private String barcode;

    @Expose
    @SerializedName("pNumberScan")
    private int numberInput;

    @Expose
    @SerializedName("pDateTimeScan")
    private String dateScan;
    private int status;

    private long userId;

    public LogScanStagesWindowModel() {
    }

    public LogScanStagesWindowModel(long orderId, int departmentIdIn, int departmentIdOut, int staffId, long productSetDetailID, String barcode, int numberInput, String dateScan, int status, long userId) {
        this.orderId = orderId;
        this.departmentIdIn = departmentIdIn;
        this.departmentIdOut = departmentIdOut;
        this.staffId = staffId;
        this.productSetDetailID = productSetDetailID;
        this.barcode = barcode;
        this.numberInput = numberInput;
        this.dateScan = dateScan;
        this.status = status;
        this.userId = userId;
    }


    public long getOrderId() {
        return orderId;
    }

    public int getDepartmentIdIn() {
        return departmentIdIn;
    }

    public int getDepartmentIdOut() {
        return departmentIdOut;
    }

    public long getUserId() {
        return userId;
    }

    public long getProductSetDetailID() {
        return productSetDetailID;
    }

    public ProductDetailWindowModel getProductDetail() {
        return productDetail;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public long getId() {
        return id;
    }

    public void setProductDetail(ProductDetailWindowModel productDetail) {
        this.productDetail = productDetail;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanStagesWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStagesWindowModel scanStages) {

        ProductDetailWindowModel productDetail = realm.where(ProductDetailWindowModel.class)
                .equalTo("productSetDetailID", scanStages.getProductSetDetailID())
                .equalTo("status",Constants.WAITING_UPLOAD)
                .equalTo("userId", scanStages.getUserId()).findFirst();

        LogScanStagesWindowModel logScanStages = realm.where(LogScanStagesWindowModel.class).equalTo("barcode", scanStages.getBarcode())
                .equalTo("status",Constants.WAITING_UPLOAD).findFirst();

        if (logScanStages == null) {
            scanStages.setId(id(realm) + 1);
            logScanStages = realm.copyToRealm(scanStages);
            logScanStages.setProductDetail(productDetail);
        } else {
            logScanStages.setNumberInput(logScanStages.getNumberInput() + scanStages.getNumberInput());
        }

        productDetail.setNumberScanned(productDetail.getNumberScanned() + scanStages.getNumberInput());
        productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
    }

    public static void updateNumberInput(Realm realm, long stagesId, int numberInput) {
        LogScanStagesWindowModel logScanStages = realm.where(LogScanStagesWindowModel.class).equalTo("id", stagesId).findFirst();
        int number = numberInput - logScanStages.getNumberInput();
        logScanStages.setNumberInput(numberInput);
        ProductDetailWindowModel productDetail = logScanStages.getProductDetail();
        productDetail.setNumberScanned(productDetail.getNumberScanned() + number);
        productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());


    }

    public static void deleteScanStages(Realm realm, long stagesId) {
        LogScanStagesWindowModel logScanStages = realm.where(LogScanStagesWindowModel.class).equalTo("id", stagesId).findFirst();
        ProductDetailWindowModel productDetail = logScanStages.getProductDetail();
        productDetail.deleteFromRealm();
        logScanStages.deleteFromRealm();

    }


    public static RealmResults<LogScanStagesWindowModel> getAllList(Realm realm) {
        RealmResults<LogScanStagesWindowModel> results = realm.where(LogScanStagesWindowModel.class)
                .equalTo("status",Constants.WAITING_UPLOAD)
                .findAll();
        return results;
    }

    public static void updateStatusLogStagesWindow(Realm realm) {
        RealmResults<LogScanStagesWindowModel> results = realm.where(LogScanStagesWindowModel.class)
                .equalTo("status",Constants.WAITING_UPLOAD)
                .findAll();
        for (LogScanStagesWindowModel model : results){
            ProductDetailWindowModel detailWindowModel = model.getProductDetail();
            detailWindowModel.setStatus(Constants.COMPLETE);
            model.setStatus(Constants.COMPLETE);
        }
    }
}
