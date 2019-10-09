package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
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

    private ProductPackagingModel productPackagingModel;

    @SerializedName("pQuantity")
    @Expose
    private int numberInput;
    private String dateScan;
    private int statusScan;
    private int status;
    private long userId;

    public LogScanPackaging() {
    }

    public LogScanPackaging(long id, long apartmentId, long productDetailId,
                            String codePack, String sttPack, String barcode,
                            ProductPackagingModel productPackagingModel, int numberInput, String dateScan,
                            int statusScan, int status, long userId) {
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
        this.userId = userId;
    }


    public static RealmResults<LogListSerialPackPagkaging> getListScanPackaging(Realm realm) {
        RealmResults<LogListSerialPackPagkaging> logListSerialPackPagkagings = realm.where(LogListSerialPackPagkaging.class)
                .sort("module").equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return logListSerialPackPagkagings;

    }

    public static void deleteLogScanPackaging(Realm realm, final long productId, final String sttPack, final String codePack, long logId) {
        LogScanPackaging logScanPackaging = realm.where(LogScanPackaging.class).equalTo("id", logId).findFirst();
        ProductPackagingModel productPackagingModel = logScanPackaging.getProductPackagingModel();
        productPackagingModel.deleteFromRealm();
        logScanPackaging.deleteFromRealm();
        LogListSerialPackPagkaging logListSerialPackPagkaging = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("productId", productId)
                .equalTo("serialPack", sttPack)
                .equalTo("codeProduct", codePack)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findFirst();
        if (logListSerialPackPagkaging.getList().size() == 0) {
            logListSerialPackPagkaging.deleteFromRealm();
        }
    }

    public static void updateNumberScanPackaging(Realm realm, long logId, int number) {
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

    public static void createOrUpdateLogScanPackaging(Realm realm, ListModuleEntity moduleEntity, PackageEntity packageEntity,
                                                      ProductPackagingEntity productPackagingEntity, long orderId, long apartmentId, long userId) {

        LogListSerialPackPagkaging logListSerialPackPagkaging = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("orderId", orderId)
                .equalTo("apartmentId", apartmentId)
                .equalTo("productId", moduleEntity.getProductId())
                .equalTo("serialPack", packageEntity.getSerialPack())
                .equalTo("codeProduct", packageEntity.getPackCode())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (logListSerialPackPagkaging == null) {
            logListSerialPackPagkaging = LogListSerialPackPagkaging.create(realm, orderId, apartmentId, packageEntity.getSerialPack(),
                    packageEntity.getPackCode(), moduleEntity.getProductId(), packageEntity.getTotal(),
                    moduleEntity.getModule());
        }
        RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
        LogScanPackaging logScanPackaging = parentList.where()
                .equalTo("barcode", productPackagingEntity.getBarcode())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                .equalTo("productId", moduleEntity.getProductId())
                .equalTo("productDetailId", productPackagingEntity.getId())
                .equalTo("serialPack", packageEntity.getSerialPack())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (productPackagingModel == null) {
            productPackagingModel = new ProductPackagingModel(id(realm) + 1, moduleEntity.getProductId(), productPackagingEntity.getId(), productPackagingEntity.getProductName(),
                    productPackagingEntity.getProductColor(), packageEntity.getSerialPack(), productPackagingEntity.getWidth(),
                    productPackagingEntity.getLength(), productPackagingEntity.getHeight(),
                    productPackagingEntity.getNumber(), 0, productPackagingEntity.getNumber(), Constants.WAITING_UPLOAD);
            productPackagingModel = realm.copyToRealm(productPackagingModel);
        }

        if (logScanPackaging == null) {
            //add productPackingModel
            logScanPackaging = new LogScanPackaging(id(realm) + 1, apartmentId, productPackagingEntity.getId(), packageEntity.getPackCode(),
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
    }

    public static List<LogScanPackaging> getListScanPackaging(Realm realm, long orderId, long apartmentId, long moduleId, String serialPack) {

        LogListSerialPackPagkaging logListSerialPackPagkaging = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("orderId", orderId)
                .equalTo("apartmentId", apartmentId)
                .equalTo("productId", moduleId)
                .equalTo("serialPack", serialPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        RealmResults<LogScanPackaging> realmList = logListSerialPackPagkaging.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(realmList);

    }

    public static void updateStatusScanPackaging(Realm realm, long logSerialId, long serverId) {
        LogListSerialPackPagkaging logListSerialPackPagkaging = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("id", logSerialId)
               .findFirst();

        logListSerialPackPagkaging.setServerId(serverId);
        logListSerialPackPagkaging.setStatus(Constants.COMPLETE);
        for (LogScanPackaging logScanPackaging : logListSerialPackPagkaging.getList()) {
            logScanPackaging.setStatus(Constants.COMPLETE);
            logScanPackaging.getProductPackagingModel().setStatus(Constants.COMPLETE);
            ProductPackagingModel productPackagingModel = logScanPackaging.getProductPackagingModel();
            productPackagingModel.setStatus(Constants.COMPLETE);
        }
    }

    public static int getTotalScan(Realm realm, long productId, String serialPack) {
        LogListSerialPackPagkaging logListSerialPackPagkaging = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("productId", productId)
                .equalTo("serialPack", serialPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        int sum = 0;
        if (logListSerialPackPagkaging != null) {
            RealmList<LogScanPackaging> parentList = logListSerialPackPagkaging.getList();
            RealmResults<LogScanPackaging> results = parentList.where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
            sum = results.sum("numberInput").intValue();
        }
        return sum;
    }

    public static void deleteAllItemLogScanPackaging(Realm realm) {
        RealmResults<LogListSerialPackPagkaging> results = realm.where(LogListSerialPackPagkaging.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        results.deleteAllFromRealm();
        RealmResults<LogScanPackaging> logScanPackagings = realm.where(LogScanPackaging.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        logScanPackagings.deleteAllFromRealm();
        RealmResults<ProductPackagingModel> productPackagingModels = realm.where(ProductPackagingModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        productPackagingModels.deleteAllFromRealm();
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


    public void setId(long id) {
        this.id = id;
    }

    public void setApartmentId(long apartmentId) {
        this.apartmentId = apartmentId;
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

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public void setProductPackagingModel(ProductPackagingModel productPackagingModel) {
        this.productPackagingModel = productPackagingModel;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public String getBarcode() {
        return barcode;
    }

    public ProductPackagingModel getProductPackagingModel() {
        return productPackagingModel;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public String getDateScan() {
        return dateScan;
    }

    public long getUserId() {
        return userId;
    }
}
