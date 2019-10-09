package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanStages extends RealmObject {

    @PrimaryKey
    private long id;
    @Expose
    @SerializedName("pOrderID")
    private long orderId;
    @Expose
    @SerializedName("pDepartmentIDIn")
    private int departmentIdIn;
    @Expose
    @SerializedName("pDepartmentIDOut")
    private int departmentIdOut;
    @Expose
    @SerializedName("pProductDetailID")
    private long productDetailId;

    private ProductDetail productDetail;

    private long masterGroupId;
    private String groupCode;
    @Expose
    @SerializedName("pBarcode")
    private String barcode;

    private String module;
    @Expose
    @SerializedName("pNumberScan")
    private int numberInput;
    //loại quét nếu quét theo gộp mã là false, quét  ko gộp mã là true
    @Expose
    @SerializedName("pTypeScan")
    private Boolean typeScan;
    @Expose
    @SerializedName("pTimes")
    private int times;
    @Expose
    @SerializedName("pDateTimeScan")
    private String dateScan;
    @Expose
    @SerializedName("pUserID")
    private long userId;

    private int numberGroup;

    private int status;

    public LogScanStages() {
    }


    public LogScanStages(long orderId, int departmentIdIn, int departmentIdOut, long productDetailId, long masterGroupId, String groupCode, String barcode, String module, int numberInput, Boolean typeScan, int times, String dateScan, long userId, int numberGroup, int status) {
        this.orderId = orderId;
        this.departmentIdIn = departmentIdIn;
        this.departmentIdOut = departmentIdOut;
        this.productDetailId = productDetailId;
        this.masterGroupId = masterGroupId;
        this.groupCode = groupCode;
        this.barcode = barcode;
        this.module = module;
        this.numberInput = numberInput;
        this.typeScan = typeScan;
        this.times = times;
        this.dateScan = dateScan;
        this.userId = userId;
        this.numberGroup = numberGroup;
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

    public int getDepartmentIdIn() {
        return departmentIdIn;
    }

    public void setDepartmentIdIn(int departmentIdIn) {
        this.departmentIdIn = departmentIdIn;
    }

    public int getDepartmentIdOut() {
        return departmentIdOut;
    }

    public void setDepartmentIdOut(int departmentIdOut) {
        this.departmentIdOut = departmentIdOut;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public void setMasterGroupId(long masterGroupId) {
        this.masterGroupId = masterGroupId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setNumberGroup(int numberGroup) {
        this.numberGroup = numberGroup;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStages scanStages, final long productId) {


        ProductDetail productDetail1 = realm.where(ProductDetail.class).equalTo("id", productId).findFirst();

        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("barcode", scanStages.getBarcode())
                .equalTo("groupCode", scanStages.groupCode).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("module", scanStages.getModule()).equalTo("typeScan", scanStages.typeScan).equalTo("times", scanStages.getTimes()).findFirst();


        if (logScanStages == null) {
            scanStages.setProductDetail(productDetail1);
            scanStages.setId(id(realm) + 1);
            logScanStages = realm.copyToRealm(scanStages);
        } else {
            logScanStages.setNumberInput(logScanStages.getNumberInput() + scanStages.getNumberInput());
            logScanStages.setNumberGroup(logScanStages.getNumberInput());
        }

        productDetail1.setNumberScanned(productDetail1.getNumberScanned() + scanStages.getNumberInput());
        productDetail1.setNumberRest(productDetail1.getNumberTotal() - productDetail1.getNumberScanned());
    }

    public static RealmResults<LogScanStages> getScanByProductDetailId(Realm realm, LogScanStages logScanStages) {

        RealmResults<LogScanStages> scanStages = realm.where(LogScanStages.class).equalTo("groupCode", logScanStages.getGroupCode())
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", logScanStages.getUserId())
                .findAll();
        return scanStages;
    }

    public static void updateNumberInput(Realm realm, long stagesId, int numberInput) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        int number = numberInput - logScanStages.getNumberInput();
        if (!logScanStages.getTypeScan()) {

            RealmResults<LogScanStages> scanStages = realm.where(LogScanStages.class).equalTo("groupCode", logScanStages.getGroupCode()).equalTo("status", Constants.WAITING_UPLOAD)
                    .equalTo("userId", logScanStages.getUserId())
                    .findAll();
            for (LogScanStages item : scanStages) {
                item.setNumberInput(item.getNumberInput() + number);
                item.setNumberGroup(item.getNumberInput() + number);
                ProductDetail productDetail = item.getProductDetail();
                productDetail.setNumberScanned(productDetail.getNumberScanned() + number);
                productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
            }

        } else {
            logScanStages.setNumberInput(numberInput);
            logScanStages.setNumberGroup(numberInput);
            ProductDetail productDetail = logScanStages.getProductDetail();
            productDetail.setNumberScanned(productDetail.getNumberScanned() + number);
            productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());

        }


    }

    public static void deleteScanStages(Realm realm, long stagesId) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();

        if (!logScanStages.getTypeScan()) {

            RealmResults<LogScanStages> scanStages = realm.where(LogScanStages.class).equalTo("groupCode", logScanStages.getGroupCode()).equalTo("status", Constants.WAITING_UPLOAD)
                    .equalTo("userId", logScanStages.getUserId())
                    .findAll();
            for (LogScanStages item : scanStages) {
                ProductDetail productDetail = item.getProductDetail();
                productDetail.setNumberScanned(productDetail.getNumberScanned() - logScanStages.getNumberInput());
                productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
            }

            scanStages.deleteAllFromRealm();
        } else {
            ProductDetail productDetail = logScanStages.getProductDetail();
            productDetail.setNumberScanned(productDetail.getNumberScanned() - logScanStages.getNumberInput());
            productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
            logScanStages.deleteFromRealm();
        }


    }


    public long getMasterGroupId() {
        return masterGroupId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Boolean getTypeScan() {
        return typeScan;
    }

    public void setTypeScan(Boolean typeScan) {
        this.typeScan = typeScan;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumberGroup() {
        return numberGroup;
    }

    public static List<LogScanStages> getListScanStagesWaitingUpload(Realm realm) {
        List<LogScanStages> list = new ArrayList<>();
        RealmResults<LogScanStages> results = realm.where(LogScanStages.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();

        list = realm.copyFromRealm(results);


        return list;
    }

    public static void updateStatusLogStages(Realm realm) {
        RealmResults<LogScanStages> results = realm.where(LogScanStages.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        for (LogScanStages item : results) {
            item.setStatus(Constants.COMPLETE);
            ProductDetail productDetail = item.getProductDetail();
            productDetail.setStatus(Constants.COMPLETE);
        }
    }
}
