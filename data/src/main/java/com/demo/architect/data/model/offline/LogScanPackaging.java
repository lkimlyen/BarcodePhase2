package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanPackaging extends RealmObject {
    @PrimaryKey
    private int id;
    @SerializedName("pApartmentID")
    @Expose
    private int apartmentId;

    @SerializedName("pProductDetailID")
    @Expose
    private int productDetailId;

    @SerializedName("pCodePack")
    @Expose
    private String codePack;

    @SerializedName("pSTTPack")
    @Expose
    private String sttPack;

    private String barcode;

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    private int module;
    private int orderId;
    private ProductPackagingModel productPackagingModel;

    @SerializedName("pQuantity")
    @Expose
    private int numberInput;
    private String dateScan;
    private int serverId;
    private int statusScan;
    private int status;
    private int userId;

    public LogScanPackaging() {
    }

    public LogScanPackaging(int id, int orderId, int module, int apartmentId, int productDetailId, String codePack, String sttPack, String barcode, ProductPackagingModel productPackagingModel, int numberInput, String dateScan, int statusScan, int status, int userId) {
        this.id = id;
        this.orderId = orderId;
        this.module = module;
        this.apartmentId = apartmentId;
        this.productDetailId = productDetailId;
        this.codePack = codePack;
        this.sttPack = sttPack;
        this.barcode = barcode;
        this.productPackagingModel = productPackagingModel;
        this.numberInput = numberInput;
        this.dateScan = dateScan;
        this.statusScan = statusScan;
        this.status = status;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ProductPackagingModel getProductPackagingModel() {
        return productPackagingModel;
    }

    public void setProductPackagingModel(ProductPackagingModel productPackagingModel) {
        this.productPackagingModel = productPackagingModel;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public static RealmResults<LogListModulePagkaging> getListScanPackaging(Realm realm, SOEntity soEntity, ApartmentEntity apartment) {

        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", soEntity.getOrderId()).findFirst();
        if (logListOrderPackaging == null) {
            realm.beginTransaction();
            logListOrderPackaging = LogListOrderPackaging.create(realm, soEntity.getOrderId(), soEntity.getCodeSO(), soEntity.getCustomerName());
            realm.commitTransaction();
        }
        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartment.getApartmentID())
                .findFirst();
        if (logListFloorPagkaging == null) {
            realm.beginTransaction();
            logListFloorPagkaging = LogListFloorPagkaging.create(realm, apartment);
            RealmList<LogListFloorPagkaging> logListFloorPagkagings = logListOrderPackaging.getList();
            logListFloorPagkagings.add(logListFloorPagkaging);

            realm.commitTransaction();
        }


        return logListFloorPagkaging.getList().where().greaterThan("size", 0).findAll();

    }

    public static LogListModulePagkaging getListScanPackaging(Realm realm, int orderId, String floor, String module) {

        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("floor", floor)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("module", module)
                .findFirst();
        return logListModulePagkaging;

    }

    public static void deleteLogScanPackaging(Realm realm, int logId) {
        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();
        logScanPackaging.setStatus(Constants.CANCEL);
        logScanPackaging.getProductPackagingModel().setStatus(Constants.CANCEL);
        LogListModulePagkaging logListModulePagkaging = realm.where(LogListModulePagkaging.class).equalTo("id", logScanPackaging.getModule()).findFirst();
        RealmResults<LogScanPackaging> realmResults = logListModulePagkaging.getLogScanPackagingList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        logListModulePagkaging.setSize(realmResults.size());
    }

    public static void updateNumberScanPackaging(Realm realm, int logId, int number) {

        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();

        int numberCurrent = number - logScanPackaging.getNumberInput();
        logScanPackaging.setNumberInput(number);
        ProductPackagingModel productPackagingModel = logScanPackaging.getProductPackagingModel();
        productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() + numberCurrent);
        productPackagingModel.setNumberRest(productPackagingModel.getNumberTotal() - productPackagingModel.getNumberScan());
        if (productPackagingModel.getNumberRest() == 0) {
            logScanPackaging.setStatusScan(Constants.FULL);
        } else {
            logScanPackaging.setStatusScan(Constants.INCOMPLETE);
        }
    }


    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanPackaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }


    public int getStatusScan() {
        return statusScan;
    }

    public void setStatusScan(int statusScan) {
        this.statusScan = statusScan;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static void createOrUpdateLogScanPackaging(Realm realm, ListModuleEntity moduleEntity, PackageEntity packageEntity, ProductPackagingEntity productPackagingEntity, int orderId, int apartmentId, int userId) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleEntity.getProductId())
                .findFirst();

        if (logListModulePagkaging == null) {
            logListModulePagkaging = LogListModulePagkaging.create(realm, moduleEntity.getModule(), moduleEntity.getProductId());
            logListFloorPagkaging.getList().add(logListModulePagkaging);
        }

        RealmList<LogScanPackaging> parentList = logListModulePagkaging.getLogScanPackagingList();
        LogScanPackaging logScanPackaging = parentList.where().equalTo("sttPack",packageEntity.getSerialPack()).equalTo("barcode", productPackagingEntity.getBarcode()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class).equalTo("productDetailId", productPackagingEntity.getId()).equalTo("serialPack", packageEntity.getSerialPack())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (productPackagingModel == null) {
            productPackagingModel = new ProductPackagingModel(id(realm) + 1, productPackagingEntity.getId(), productPackagingEntity.getProductName(),
                    productPackagingEntity.getProductColor(), packageEntity.getSerialPack(), productPackagingEntity.getWidth(),
                    productPackagingEntity.getLength(), productPackagingEntity.getHeight(),
                    productPackagingEntity.getNumber(), 0, productPackagingEntity.getNumber(), Constants.WAITING_UPLOAD);

            productPackagingModel = realm.copyToRealm(productPackagingModel);
        }

        if (logScanPackaging == null) {
            //add productPackingModel
            logScanPackaging = new LogScanPackaging(id(realm) + 1, orderId, moduleEntity.getProductId(), apartmentId, productPackagingEntity.getId(), packageEntity.getPackCode(),
                    packageEntity.getSerialPack(), productPackagingEntity.getBarcode(), productPackagingModel, 1,
                    DateUtils.getDateTimeCurrent(), Constants.INCOMPLETE, Constants.WAITING_UPLOAD, userId);
            logScanPackaging = realm.copyToRealm(logScanPackaging);
            parentList.add(logScanPackaging);
            logScanPackaging.setProductPackagingModel(productPackagingModel);

        } else {
            logScanPackaging.setNumberInput(logScanPackaging.getNumberInput() + 1);
        }
        productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() + 1);
        productPackagingModel.setNumberRest(productPackagingModel.getNumberTotal() - productPackagingModel.getNumberScan());
        if (productPackagingModel.getNumberRest() == 0) {
            logScanPackaging.setStatusScan(Constants.FULL);
        } else {
            logScanPackaging.setStatusScan(Constants.INCOMPLETE);
        }

        logListModulePagkaging.setSize(logListModulePagkaging.getLogScanPackagingList().size());
    }

    public static List<LogScanPackaging> getListScanPackaging(Realm realm, int orderId, int apartmentId, int moduleId, String serialPack) {

        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();
        RealmResults<LogScanPackaging> realmList = logListModulePagkaging.getLogScanPackagingList().where().equalTo("sttPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findAll();

        return realm.copyFromRealm(realmList);

    }

    public static void updateStatusScanPackaging(Realm realm, int orderId, int apartmentId, int moduleId, String serialPack, int serverId) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();
        RealmResults<LogScanPackaging> results = logListModulePagkaging.getLogScanPackagingList().where().equalTo("sttPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        RealmResults<LogScanPackaging> realmResults = logListModulePagkaging.getLogScanPackagingList().where().notEqualTo("sttPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findAll();

           logListModulePagkaging.setSize(realmResults.size());

        for (LogScanPackaging logScanPackaging : results) {
            logScanPackaging.setStatus(Constants.COMPLETE);
            logScanPackaging.setServerId(serverId);
            logScanPackaging.getProductPackagingModel().setStatus(Constants.COMPLETE);
        }
    }

    public static int getTotalScan(Realm realm, int orderId, int apartmentId, int moduleId, String serialPack) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();
        RealmResults<LogScanPackaging> results = logListModulePagkaging.getLogScanPackagingList().where().equalTo("sttPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findAll();

        int sum = results.sum("numberInput").intValue();
        return sum;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public static void deleteAllItemLogScanPackaging(Realm realm) {
        RealmResults<LogScanPackaging> results = realm.where(LogScanPackaging.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        for (LogScanPackaging logScanPackaging : results) {
            logScanPackaging.setStatus(Constants.CANCEL);
            logScanPackaging.getProductPackagingModel().setStatus(Constants.CANCEL);
        }
    }

    public String getCodePack() {
        return codePack;
    }

    public String getSttPack() {
        return sttPack;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }
}
