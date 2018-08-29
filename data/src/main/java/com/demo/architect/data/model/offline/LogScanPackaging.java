package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.utils.view.DateUtils;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanPackaging extends RealmObject {
    @PrimaryKey
    private int id;
    private String barcode;
    private ProductPackagingModel productPackagingModel;
    private int numberInput;
    private String dateScan;
    private int statusScan;
    private int status;

    public LogScanPackaging() {
    }

    public LogScanPackaging(int id, String barcode, ProductPackagingModel productPackagingModel, int numberInput, String dateScan, int statusScan, int status) {
        this.id = id;
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

    public static LogListModulePagkaging getListScanPackaging(Realm realm, int orderId, int productId, int apartmentId, String packcode, String sttPack) {

        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();
        if (logListOrderPackaging == null) {
            realm.beginTransaction();
            logListOrderPackaging = LogListOrderPackaging.create(realm, orderId, "", "");
            realm.commitTransaction();
        }
        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("id", apartmentId)
                .findFirst();
        if (logListFloorPagkaging == null) {
            realm.beginTransaction();
            logListFloorPagkaging = LogListFloorPagkaging.create(realm, floor);
            RealmList<LogListFloorPagkaging> logListFloorPagkagings = logListOrderPackaging.getList();
            logListFloorPagkagings.add(logListFloorPagkaging);

            realm.commitTransaction();
        }

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("module", module)
                .findFirst();
        if (logListModulePagkaging == null) {
            realm.beginTransaction();
            logListModulePagkaging = LogListModulePagkaging.create(realm, module);
            RealmList<LogListModulePagkaging> logListModulePagkagings = logListFloorPagkaging.getList();
            logListModulePagkagings.add(logListModulePagkaging);

            realm.commitTransaction();
        }

        RealmResults<LogListSerialPackPagkaging> results = logListModulePagkaging.getList().where()
                .findAll();
        RealmList<LogListSerialPackPagkaging> parentList = logListModulePagkaging.getList();
        if (results.size() == 0) {
            realm.beginTransaction();
            for (Map.Entry<String, String> map : packList.entrySet()) {
                parentList.add(LogListSerialPackPagkaging.create(realm, map.getValue(), map.getKey()));
            }

            realm.commitTransaction();
        }
        return logListModulePagkaging;

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

    public static void createOrUpdateLogScanPackaging(Realm realm, ProductPackagingEntity entity,
                                                      int orderId, String floor, String module,
                                                      String barcode) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();

        LogListFloorPagkaging logListFloorPagkaging = logListOrderPackaging.getList().where().equalTo("floor", floor)
                .findFirst();

        LogListModulePagkaging logListModulePagkaging = logListFloorPagkaging.getList().where().equalTo("module", module)
                .findFirst();

        LogListSerialPackPagkaging logListSerialPackPagkaging = logListModulePagkaging.getList().where()
                .equalTo("serialPack", entity.getSerialPack()).findFirst();

        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        LogScanPackaging logScanPackaging = parentList.where().equalTo("barcode", barcode).equalTo("status",Constants.WAITING_UPLOAD).findFirst();
        if (logScanPackaging == null) {
            //add productPackingModel
            ProductPackagingModel productPackagingModel = new ProductPackagingModel();
            logScanPackaging = new LogScanPackaging(id(realm) + 1, barcode, productPackagingModel, 1, DateUtils.getDateTimeCurrent(), Constants.INCOMPLETE,Constants.WAITING_UPLOAD);
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
}
