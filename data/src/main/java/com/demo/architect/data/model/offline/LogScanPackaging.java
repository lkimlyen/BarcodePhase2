package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
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

    private ProductPackagingModel productPackagingModel;

    @SerializedName("pQuantity")
    @Expose
    private int numberInput;
    private String dateScan;
    private int serverId;
    private int statusScan;
    private int status;

    public LogScanPackaging() {
    }

    public LogScanPackaging(int id, int apartmentId, int productDetailId, String codePack, String sttPack, String barcode, ProductPackagingModel productPackagingModel, int numberInput, String dateScan, int statusScan, int status) {
        this.id = id;
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

    public static LogListSerialPackPagkaging getListScanPackaging(Realm realm, SOEntity soEntity, ModuleEntity moduleEntity, ApartmentEntity apartment, CodePackEntity codePack) {

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

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleEntity.getProductId())
                .findFirst();
        if (logListModulePagkaging == null) {
            realm.beginTransaction();
            logListModulePagkaging = LogListModulePagkaging.create(realm, moduleEntity);
            RealmList<LogListModulePagkaging> logListModulePagkagings = logListFloorPagkaging.getList();
            logListModulePagkagings.add(logListModulePagkaging);

            realm.commitTransaction();
        }

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListModulePagkaging.getList().where().equalTo("serialPack", codePack.getSttPack())
                .equalTo("codeProduct", codePack.getPackCode()).findFirst();
        if (logListSerialPackPagkaging == null) {
            realm.beginTransaction();
            logListSerialPackPagkaging = LogListSerialPackPagkaging.create(realm, codePack);
            RealmList<LogListSerialPackPagkaging> logListSerialPackPagkagings = logListModulePagkaging.getList();
            logListSerialPackPagkagings.add(logListSerialPackPagkaging);

            realm.commitTransaction();
        }
        return logListSerialPackPagkaging;

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

        ProductPackagingModel productPackagingModel = logScanPackaging.getProductPackagingModel();
        productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() - logScanPackaging.getNumberInput());
        productPackagingModel.setNumberRest(productPackagingModel.getNumberTotal() - productPackagingModel.getNumberScan());

        logScanPackaging.setStatus(Constants.CANCEL);
        //logScanPackaging.deleteFromRealm();
    }

    public static void updateNumberScanPackaging(Realm realm, int logId, int number) {

        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();

        int numberCurrent = number - logScanPackaging.getNumberInput();
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

    public static void createOrUpdateLogScanPackaging(Realm realm, ProductPackagingEntity product, String barcode, int orderId, int apartmentId, int moduleId, String packCode, String serialPack) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListModulePagkaging.getList().where()
                .equalTo("serialPack", serialPack)
                .equalTo("codeProduct", packCode).findFirst();

        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        LogScanPackaging logScanPackaging = parentList.where().equalTo("barcode", barcode).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (logScanPackaging == null) {
            //add productPackingModel
            ProductPackagingModel productPackagingModel = new ProductPackagingModel(product.getId(), product.getProductName(),
                    product.getProductColor(), product.getWidth(), product.getLength(), product.getHeight(), product.getNumber(), product.getNumber(), 0);
            logScanPackaging = new LogScanPackaging(id(realm) + 1, apartmentId, product.getId(), packCode, serialPack, barcode, productPackagingModel, 1, DateUtils.getDateTimeCurrent(), Constants.INCOMPLETE, Constants.WAITING_UPLOAD);
            logScanPackaging = realm.copyToRealm(logScanPackaging);
            parentList.add(logScanPackaging);
        } else {
            logScanPackaging.setNumberInput(logScanPackaging.getNumberInput() + 1);
            ProductPackagingModel productPackagingModel = logScanPackaging.getProductPackagingModel();
            productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() + 1);
            productPackagingModel.setNumberRest(productPackagingModel.getNumberTotal() - productPackagingModel.getNumberScan());
            if (productPackagingModel.getNumberRest() == 0) {
                logScanPackaging.setStatusScan(Constants.FULL);
            } else {
                logScanPackaging.setStatusScan(Constants.INCOMPLETE);
            }
        }
    }

    public static List<LogScanPackaging> getListScanPackaging(Realm realm, int orderId, int apartmentId, int moduleId, String serialPack) {

        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListModulePagkaging.getList().where().equalTo("serialPack", serialPack).findFirst();

        RealmResults<LogScanPackaging> realmList = logListSerialPackPagkaging.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        return realm.copyFromRealm(realmList);

    }

    public static void updateStatusScanPackaging(Realm realm, int serverId) {
        RealmResults<LogScanPackaging> results = realm.where(LogScanPackaging.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        for (LogScanPackaging logScanPackaging : results) {
            logScanPackaging.setStatus(Constants.COMPLETE);
            logScanPackaging.setServerId(serverId);
        }
    }

    public static int getTotalScan(Realm realm, int orderId, int apartmentId, int moduleId, String serialPack) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("id", moduleId)
                .findFirst();

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListModulePagkaging.getList().where().equalTo("serialPack", serialPack).findFirst();
        RealmResults<LogScanPackaging> results = logListSerialPackPagkaging.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        int sum = results.sum("numberInput").intValue();
        return sum;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
