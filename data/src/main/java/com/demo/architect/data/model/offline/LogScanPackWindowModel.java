package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogScanPackWindowModel extends RealmObject {
    @PrimaryKey
    private long id;
    @SerializedName("pProductSetDetailID")
    @Expose
    private long productSetDetailId;

    //sl trên 1 gói
    @SerializedName("pNumberOnSet")
    @Expose
    private int numberOnSet;
    private String barcode;
    //sl quét
    @SerializedName("pNumberScan")
    @Expose
    private int numberScan;
    private String dateScan;
    private int statusScan;
    private int status;
    private long userId;
    private ProductPackWindowModel productPackWindowModel;

    public LogScanPackWindowModel() {
    }

    public LogScanPackWindowModel(long productSetDetailId, int numberOnSet, String barcode, int numberScan) {
        this.productSetDetailId = productSetDetailId;
        this.numberOnSet = numberOnSet;
        this.barcode = barcode;
        this.numberScan = numberScan;
    }

    public LogScanPackWindowModel(long id, long productSetDetailId, int numberOnSet, String barcode, int numberScan, String dateScan, int statusScan, int status, long userId) {
        this.id = id;
        this.productSetDetailId = productSetDetailId;
        this.numberOnSet = numberOnSet;
        this.barcode = barcode;
        this.numberScan = numberScan;
        this.dateScan = dateScan;
        this.statusScan = statusScan;
        this.status = status;
        this.userId = userId;
    }

    public static RealmResults<ListPackCodeWindowModel> getListScanPackaging(Realm realm) {
        //lấy ds quét sắp xếp theo mã gói
        RealmResults<ListPackCodeWindowModel> logListSerialPackPagkagings = realm.where(ListPackCodeWindowModel.class)
                .sort("packCode").equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return logListSerialPackPagkagings;

    }

    public static void deleteLogScanPackaging(Realm realm, final long logId, final String packCode, final int numberOnPack) {
        LogScanPackWindowModel logScanPackaging = realm.where(LogScanPackWindowModel.class).equalTo("id", logId).findFirst();
        //lấy chi tiết update lại sl trước khi xóa LogScanPackWindowModel
        ProductPackWindowModel productPackagingModel = logScanPackaging.getProductPackWindowModel();
        productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() - logScanPackaging.getNumberScan());
        productPackagingModel.setNumberRest(productPackagingModel.getNumberRest() + logScanPackaging.getNumberScan());
        logScanPackaging.deleteFromRealm();
        //lấy gói có chi tiết đã xóa
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("packCode", packCode)
                .equalTo("numberSetOnPack", numberOnPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        //kiểm tra xem còn chi tiết
        if (main.getList().size() == 0) {
            main.deleteFromRealm();
        }
    }

    public static void updateNumberScanPackaging(Realm realm, String packCode, int numberOnPack, long logId, int number) {
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("packCode", packCode)
                .equalTo("numberSetOnPack", numberOnPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        LogScanPackWindowModel logScanPackaging = main.getList().where().equalTo("id", logId).findFirst();
        int numberCurrent = number - logScanPackaging.getNumberScan();
        logScanPackaging.setNumberScan(number);
        ProductPackWindowModel productPackagingModel = logScanPackaging.getProductPackWindowModel();
        productPackagingModel.setNumberScan(productPackagingModel.getNumberScan() + numberCurrent);
        productPackagingModel.setNumberRest(productPackagingModel.getNumberRest() - numberCurrent);

        if (logScanPackaging.getNumberScan() < main.getNumberSetOnPack() * logScanPackaging.getNumberOnSet()) {
            logScanPackaging.setStatusScan(Constants.INCOMPLETE);
        } else {
            logScanPackaging.setStatusScan(Constants.FULL);
        }
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogScanPackWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static boolean checkCondition(Realm realm, long productId, GroupSetEntity groupSetEntity) {
        ProductPackWindowModel productPackWindowModel = realm.where(ProductPackWindowModel.class)
                .equalTo("id", productId).findFirst();
        boolean satisfy;
        //check điều kiện để save
        if (productPackWindowModel != null) {

            ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                    .equalTo("packCode", groupSetEntity.getPackCode())
                    .equalTo("numberSetOnPack", groupSetEntity.getNumberSetOnPack())
                    .equalTo("status", Constants.WAITING_UPLOAD).findFirst();

            if (main != null) {
                RealmList<LogScanPackWindowModel> parentList = main.getList();
                LogScanPackWindowModel logScanPackaging = parentList.where()
                        .equalTo("barcode", productPackWindowModel.getBarcode())
                        .equalTo("status", Constants.WAITING_UPLOAD).findFirst();

                if (logScanPackaging == null) {
                    satisfy = true;
                } else {
                    if (logScanPackaging.getNumberScan() < (groupSetEntity.getNumberOnSet() * groupSetEntity.getNumberSetOnPack())) {
                        satisfy = true;
                    } else {
                        satisfy = false;
                    }
                }
            } else {
                satisfy = true;
            }
        } else {
            satisfy = true;
        }
        return satisfy;
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

    public static void createOrUpdateLogScanPackaging(Realm realm, long productId, int direction,
                                                      GroupSetEntity groupSetEntity, long userId) {
        ProductPackWindowModel productPackWindowModel = realm.where(ProductPackWindowModel.class)
                .equalTo("id", productId).findFirst();
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("packCode", groupSetEntity.getPackCode())
                .equalTo("numberSetOnPack", groupSetEntity.getNumberSetOnPack())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (main == null) {
            main = new ListPackCodeWindowModel(ListPackCodeWindowModel.id(realm) + 1, productPackWindowModel.getOrderId(),
                    productPackWindowModel.getProductSetDetailId(), groupSetEntity.getPackCode()
                    , direction, groupSetEntity.getNumberSetOnPack(), groupSetEntity.getNumberTotal(), 0, userId, Constants.WAITING_UPLOAD);
            main = realm.copyToRealm(main);
        }
        RealmList<LogScanPackWindowModel> parentList = main.getList();
        LogScanPackWindowModel logScanPackaging = parentList.where()
                .equalTo("barcode", productPackWindowModel.getBarcode())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();

        if (logScanPackaging == null) {
            logScanPackaging = new LogScanPackWindowModel(id(realm) + 1, productPackWindowModel.getProductSetDetailId(),
                    groupSetEntity.getNumberOnSet(), productPackWindowModel.getBarcode(), 1,
                    DateUtils.getDateTimeCurrent(), Constants.INCOMPLETE, Constants.WAITING_UPLOAD, userId);
            logScanPackaging = realm.copyToRealm(logScanPackaging);
            parentList.add(logScanPackaging);
            logScanPackaging.setProductPackWindowModel(productPackWindowModel);
        } else {
            logScanPackaging.setNumberScan(logScanPackaging.getNumberScan() + 1);
        }
        productPackWindowModel.setNumberScan(productPackWindowModel.getNumberScan() + 1);
        productPackWindowModel.setNumberRest(productPackWindowModel.getNumberRest() - 1);
        //kiểm tra chi tiết đã quét đủ  chưa để set trạng thái cho cho tiết
        if (logScanPackaging.getNumberScan() < main.getNumberSetOnPack() * logScanPackaging.getNumberOnSet()) {
            logScanPackaging.setStatusScan(Constants.INCOMPLETE);
        } else {
            logScanPackaging.setStatusScan(Constants.FULL);
        }
    }


    public static void updateStatusScanPackaging(Realm realm, long mainId, long serverId) {
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("id", mainId)
                .findFirst();

        main.setServerId(serverId);
        main.setStatus(Constants.COMPLETE);
        for (LogScanPackWindowModel logScanPackaging : main.getList()) {
            logScanPackaging.setStatus(Constants.COMPLETE);

        }
    }

    public static int getTotalScan(Realm realm, String packCode, int numberPack) {
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("packCode", packCode)
                .equalTo("numberSetOnPack", numberPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        int sum = 0;
        if (main != null) {
            RealmList<LogScanPackWindowModel> parentList = main.getList();
            RealmResults<LogScanPackWindowModel> results = parentList.where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
            sum = results.sum("numberScan").intValue();
        }
        return sum;
    }

    public static void deleteAllItemLogScanPackaging(Realm realm) {
        RealmResults<ListPackCodeWindowModel> results = realm.where(ListPackCodeWindowModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        results.deleteAllFromRealm();
        RealmResults<LogScanPackWindowModel> logScanPackagings = realm.where(LogScanPackWindowModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        logScanPackagings.deleteAllFromRealm();
        RealmResults<ProductPackWindowModel> productPackagingModels = realm.where(ProductPackWindowModel.class)
                .findAll();
        productPackagingModels.deleteAllFromRealm();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public void setProductSetDetailId(long productSetDetailId) {
        this.productSetDetailId = productSetDetailId;
    }

    public int getNumberOnSet() {
        return numberOnSet;
    }

    public void setNumberOnSet(int numberOnSet) {
        this.numberOnSet = numberOnSet;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getNumberScan() {
        return numberScan;
    }

    public void setNumberScan(int numberScan) {
        this.numberScan = numberScan;
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

    public ProductPackWindowModel getProductPackWindowModel() {
        return productPackWindowModel;
    }

    public void setProductPackWindowModel(ProductPackWindowModel productPackWindowModel) {
        this.productPackWindowModel = productPackWindowModel;
    }

    public static int getNumberScanWindowByBarcode(Realm realm, String packCode, int numberSetOnPack, String barcode) {
        ListPackCodeWindowModel main = realm.where(ListPackCodeWindowModel.class)
                .equalTo("packCode", packCode)
                .equalTo("numberSetOnPack", numberSetOnPack)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        int numberScan = 0;
        if (main != null) {
            RealmList<LogScanPackWindowModel> parentList = main.getList();
            LogScanPackWindowModel results = parentList.where().equalTo("barcode", barcode).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
            if (results != null) {
                numberScan = results.getNumberScan();
            }
        }
        return numberScan;
    }
}
