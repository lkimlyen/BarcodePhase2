package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanStages extends RealmObject {

    @PrimaryKey
    private int id;

    @Expose
    @SerializedName("pOrderID")
    private int orderId;
    @Expose
    @SerializedName("pDepartmentIDIn")
    private int departmentIdIn;
    @Expose
    @SerializedName("pDepartmentIDOut")
    private int departmentIdOut;
    @Expose
    @SerializedName("pProductDetailID")
    private int productDetailId;

    private ProductDetail productDetail;

    @Expose
    @SerializedName("pBarcode")
    private String barcode;

    private String module;
    @Expose
    @SerializedName("pNumberScan")
    private int numberInput;
    @Expose
    @SerializedName("pTimes")
    private int times;
    @Expose
    @SerializedName("pDateTimeScan")
    private String dateScan;
    @Expose
    @SerializedName("pUserID")
    private int userId;

    public LogScanStages() {
    }


    public LogScanStages(int orderId, int departmentIdIn, int departmentIdOut, int productDetailId, String barcode, String module, int numberInput, int times, String dateScan, int userId) {
        this.orderId = orderId;
        this.departmentIdIn = departmentIdIn;
        this.departmentIdOut = departmentIdOut;
        this.productDetailId = productDetailId;
        this.barcode = barcode;
        this.module = module;
        this.numberInput = numberInput;
        this.times = times;
        this.dateScan = dateScan;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getModule() {
        return module;
    }


    public int getNumberInput() {
        return numberInput;
    }

    public int getTimes() {
        return times;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
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

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStages scanStages) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", scanStages.getOrderId()).findFirst();
        if (mainParent == null) {
            mainParent = new LogListScanStagesMain(scanStages.getOrderId());
            mainParent = realm.copyToRealm(mainParent);
        }

        RealmList<LogListScanStages> mainParentList = mainParent.getList();

        LogListScanStages parent = realm.where(LogListScanStages.class).equalTo("departmentId", scanStages.getDepartmentIdOut())
                .equalTo("date", DateUtils.getShortDateCurrent()).equalTo("userId", scanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (parent == null) {
            parent = new LogListScanStages(LogListScanStages.id(realm) + 1, scanStages.getDepartmentIdOut(), Constants.WAITING_UPLOAD, scanStages.getUserId(), DateUtils.getShortDateCurrent());
            parent = realm.copyToRealm(parent);
            mainParentList.add(parent);
        }
        RealmList<LogScanStages> parentList = parent.getList();
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", scanStages.getProductDetailId()).findFirst();

        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("barcode", scanStages.getBarcode())
                .equalTo("module", scanStages.getModule()).equalTo("times", scanStages.getTimes()).findFirst();
        if (logScanStages == null) {
            scanStages.setProductDetail(productDetail);
            scanStages.setId(id(realm)+1);
            logScanStages = realm.copyToRealm(scanStages);
            parentList.add(logScanStages);
        } else {
            logScanStages.setNumberInput(logScanStages.getNumberInput() + scanStages.getNumberInput());
        }
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", scanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + scanStages.getNumberInput());
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
    }

    public static void updateNumberInput(Realm realm, int stagesId, int numberInput) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        int number = logScanStages.getNumberInput() - numberInput;
        logScanStages.setNumberInput(numberInput);
        ProductDetail productDetail = logScanStages.getProductDetail();
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + number);
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
    }

    public static void deleteScanStages(Realm realm, int stagesId) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        ProductDetail productDetail = logScanStages.getProductDetail();
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() - logScanStages.getNumberInput());
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
        logScanStages.deleteFromRealm();
    }


}
