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
    private long id;
    @SerializedName("pApartmentID")
    @Expose
    private long apartmentId;

    @SerializedName("pProductDetailID")
    @Expose
    private long productDetailId;

    @SerializedName("pCodePack")
    @Expose
    private String codePack;

    @SerializedName("pSTTPack")
    @Expose
    private String sttPack;

    private String barcode;

    public long getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    private long module;
    private long orderId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setApartmentId(long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public void setCodePack(String codePack) {
        this.codePack = codePack;
    }

    public void setSttPack(String sttPack) {
        this.sttPack = sttPack;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setModule(long module) {
        this.module = module;
    }

    public ProductPackagingModel getProductPackagingModel() {
        return productPackagingModel;
    }

    public void setProductPackagingModel(ProductPackagingModel productPackagingModel) {
        this.productPackagingModel = productPackagingModel;
    }

    public double getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(double numberInput) {
        this.numberInput = numberInput;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    private ProductPackagingModel productPackagingModel;

    @SerializedName("pQuantity")
    @Expose
    private double numberInput;
    private String dateScan;
    private long serverId;
    private int statusScan;
    private int status;
    private long userId;

    public LogScanPackaging() {
    }

    public LogScanPackaging(long id, long orderId, long module, long apartmentId, long productDetailId, String codePack, String sttPack, String barcode, ProductPackagingModel productPackagingModel, double numberInput, String dateScan, int statusScan, int status, long userId) {
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


    public static RealmResults<LogListSerialPackPagkaging> getListScanPackaging(Realm realm, SOEntity soEntity, ApartmentEntity apartment) {

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
        RealmResults<LogListSerialPackPagkaging> logListSerialPackPagkagings = logListFloorPagkaging.getList().where().greaterThan("size", 0).sort("module").findAll();
        return logListSerialPackPagkagings;

    }

    public static void deleteLogScanPackaging(Realm realm, long logId) {
        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();
        logScanPackaging.setStatus(Constants.CANCEL);
        logScanPackaging.getProductPackagingModel().setStatus(Constants.CANCEL);
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", logScanPackaging.getOrderId()).findFirst();
        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", logScanPackaging.getApartmentId())
                .findFirst();
        RealmList<LogListSerialPackPagkaging> realmList = logListFloorPagkaging.getList();
        LogListSerialPackPagkaging logListSerialPackPagkaging = realmList.where().equalTo("serialPack", logScanPackaging.getSttPack()).findFirst();
        logListSerialPackPagkaging.setSize(logListSerialPackPagkaging.getSize() - 1);
    }

    public static void updateNumberScanPackaging(Realm realm, long logId, double number) {

        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();

        double numberCurrent = number - logScanPackaging.getNumberInput();
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


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanPackaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
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

    public static void createOrUpdateLogScanPackaging(Realm realm, ListModuleEntity moduleEntity, PackageEntity packageEntity, ProductPackagingEntity productPackagingEntity, long orderId, long apartmentId, long userId) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

//        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleEntity.getProductSetDetailID())
//                .findFirst();
//
//        if (logListModulePagkaging == null) {
//            logListModulePagkaging = LogListModulePagkaging.create(realm, moduleEntity.getProductSetName(), moduleEntity.getProductSetDetailID());
//            logListFloorPagkaging.getList().add(logListModulePagkaging);
//        }


        LogListSerialPackPagkaging logListSerialPackPagkaging = logListFloorPagkaging.getList().where()
                .equalTo("productId",moduleEntity.getProductId())
                .equalTo("module",moduleEntity.getModule())
                .equalTo("serialPack", packageEntity.getSerialPack())
                .equalTo("codeProduct", packageEntity.getPackCode()).findFirst();
        if (logListSerialPackPagkaging == null) {
            logListSerialPackPagkaging = LogListSerialPackPagkaging.create(realm, packageEntity.getSerialPack(), packageEntity.getPackCode(), moduleEntity.getProductId(), packageEntity.getTotal(), moduleEntity.getModule());
            logListFloorPagkaging.getList().add(logListSerialPackPagkaging);
        }else{
            logListSerialPackPagkaging.setNumberTotal(packageEntity.getTotal());
        }
        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        LogScanPackaging logScanPackaging = parentList.where().equalTo("sttPack", packageEntity.getSerialPack()).equalTo("barcode", productPackagingEntity.getBarcode()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
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
        logListSerialPackPagkaging.setSize(logListSerialPackPagkaging.getSize() + 1);
    }

    public static List<LogScanPackaging> getListScanPackaging(Realm realm, long orderId, long apartmentId, long moduleId, String serialPack) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

//        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
//                .findFirst();

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListFloorPagkaging.getList().where()
                .equalTo("productId",moduleId)
                .equalTo("serialPack", serialPack).findFirst();
        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        RealmResults<LogScanPackaging> realmList = parentList.where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        return realm.copyFromRealm(realmList);

    }

    public static void updateStatusScanPackaging(Realm realm, long orderId, long apartmentId, long moduleId, String serialPack, long serverId) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

//        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
//                .findFirst();
        LogListSerialPackPagkaging logListSerialPackPagkaging = logListFloorPagkaging.getList().where()
                .equalTo("productId",moduleId)
                .equalTo("serialPack", serialPack).findFirst();
        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        RealmResults<LogScanPackaging> results = parentList.where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
        logListSerialPackPagkaging.setSize(logListSerialPackPagkaging.getSize() - results.size());

        for (LogScanPackaging logScanPackaging : results) {
            logScanPackaging.setStatus(Constants.COMPLETE);
            logScanPackaging.setServerId(serverId);
            logScanPackaging.getProductPackagingModel().setStatus(Constants.COMPLETE);
        }
    }

    public static int getTotalScan(Realm realm, long orderId, long apartmentId, long moduleId, String serialPack) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

//        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
//                .findFirst();
        LogListSerialPackPagkaging logListSerialPackPagkaging = logListFloorPagkaging.getList().where()
                .equalTo("productId",moduleId) .equalTo("serialPack", serialPack).findFirst();
        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        RealmResults<LogScanPackaging> results = parentList.where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        int sum = results.sum("numberInput").intValue();
        return sum;
    }

    public static void deleteAllItemLogScanPackaging(Realm realm) {
        RealmResults<LogScanPackaging> results = realm.where(LogScanPackaging.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        for (LogScanPackaging logScanPackaging : results) {
            logScanPackaging.setStatus(Constants.CANCEL);
            logScanPackaging.getProductPackagingModel().setStatus(Constants.CANCEL);
        }
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public String getCodePack() {
        return codePack;
    }

    public String getSttPack() {
        return sttPack;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getServerId() {
        return serverId;
    }

}
