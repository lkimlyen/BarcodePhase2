package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private double numberInput;

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

    private double numberGroup;

    public LogScanStages() {
    }


    public LogScanStages(long orderId, int departmentIdIn, int departmentIdOut, long productDetailId, long masterGroupId, String groupCode, String barcode, String module, double numberInput, Boolean typeScan, int times, String dateScan, long userId, double numberGroup) {
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

    public double getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(double numberInput) {
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

    public void setNumberGroup(double numberGroup) {
        this.numberGroup = numberGroup;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStages scanStages, ProductEntity productEntity) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", scanStages.getOrderId()).findFirst();

        LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", scanStages.getDepartmentIdIn())
                .equalTo("times", scanStages.getTimes()).equalTo("userId", scanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();

        RealmList<LogScanStages> parentList = parent.getList();
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", scanStages.getProductDetailId())
                .equalTo("userId", scanStages.getUserId()).findFirst();
        if (productDetail == null) {
            productDetail = ProductDetail.create(realm, productEntity, scanStages.userId);
        }LogScanStages


            logScanStages   = parent.getList().where().equalTo("barcode", scanStages.getBarcode())
                    .equalTo("groupCode",scanStages.groupCode )
                    .equalTo("module", scanStages.getModule()).equalTo("typeScan", scanStages.typeScan).equalTo("times", scanStages.getTimes()).findFirst();


        if (logScanStages == null) {
            scanStages.setProductDetail(productDetail);
            scanStages.setId(id(realm) + 1);
            logScanStages = realm.copyToRealm(scanStages);
            parentList.add(logScanStages);
        } else {
            logScanStages.setNumberInput(logScanStages.getNumberInput() + scanStages.getNumberInput());
            logScanStages.setNumberGroup(logScanStages.getNumberInput());
        }
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", scanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + scanStages.getNumberInput());
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
    }

    public static RealmResults<LogScanStages> getScanByProductDetailId(Realm realm, LogScanStages logScanStages){
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", logScanStages.getOrderId()).findFirst();

        LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", logScanStages.getDepartmentIdIn())
               .equalTo("times", logScanStages.getTimes())
                .equalTo("userId",logScanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();


        RealmList<LogScanStages> parentList = parent.getList();
        RealmResults<LogScanStages> scanStages = parentList.where().equalTo("groupCode", logScanStages.getGroupCode())
                .equalTo("userId", logScanStages.getUserId())
                .findAll();
        return scanStages;
    }

    public static void updateNumberInput(Realm realm, long stagesId, double numberInput) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        double number = numberInput - logScanStages.getNumberInput();
        if (!logScanStages.getTypeScan()) {
            LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", logScanStages.getOrderId()).findFirst();

            LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", logScanStages.getDepartmentIdIn())
                  .equalTo("times", logScanStages.getTimes())
                    .equalTo("userId",logScanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();


            RealmList<LogScanStages> parentList = parent.getList();
            RealmResults<LogScanStages> scanStages = parentList.where().equalTo("groupCode", logScanStages.getGroupCode())
                    .equalTo("userId", logScanStages.getUserId())
                    .findAll();
            for (LogScanStages item : scanStages) {
                item.setNumberInput(item.getNumberInput() + number);
                item.setNumberGroup(item.getNumberInput() + number);
                ProductDetail productDetail = item.getProductDetail();
                NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
                numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + number);
                numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
            }

        } else {
            logScanStages.setNumberInput(numberInput);
            logScanStages.setNumberGroup(numberInput);
            ProductDetail productDetail = logScanStages.getProductDetail();
            NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
            numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + number);
            numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());

        }


    }

    public static void deleteScanStages(Realm realm, long stagesId) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();

        if (!logScanStages.getTypeScan()) {
            LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", logScanStages.getOrderId()).findFirst();

            LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", logScanStages.getDepartmentIdIn())
                   .equalTo("times", logScanStages.getTimes())
                    .equalTo("userId",logScanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();


            RealmList<LogScanStages> parentList = parent.getList();
            RealmResults<LogScanStages> scanStages = parentList.where().equalTo("groupCode", logScanStages.getGroupCode())
                    .equalTo("userId", logScanStages.getUserId())
                    .findAll();
            for (LogScanStages item : scanStages) {
                ProductDetail productDetail = item.getProductDetail();
                NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
                numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() - logScanStages.getNumberInput());
                numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
            }

            scanStages.deleteAllFromRealm();
        } else {
            ProductDetail productDetail = logScanStages.getProductDetail();
            NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
            numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() - logScanStages.getNumberInput());
            numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
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

    public double getNumberGroup() {
        return numberGroup;
    }
}
