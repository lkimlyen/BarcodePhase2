package com.demo.architect.data.model.offline;

import android.text.TextUtils;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductWindowEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class QualityControlWindowModel extends RealmObject {

    @PrimaryKey
    private long id;
    @SerializedName("pBarcode")
    @Expose
    private String barcode;
    private String productSetName;

    private int machineId;

    private String violator;

    private int qcId;

    private int departmentId;

    private long orderId;

    @SerializedName("pProductSetDetailID")
    @Expose
    private long productSetDetailId;
    private String productSetDetailName;
    private int totalNumber;

    @SerializedName("pListReason")
    @Expose
    private String listReason;

    @SerializedName("pNote")
    @Expose
    private String description;
    private RealmList<Integer> idReasonList;

    @SuppressWarnings("unused")
    private RealmList<ImageModel> imageList;

    @SerializedName("pListImage")
    @Expose
    private String listImage;

    @SerializedName("pNumber")
    @Expose
    private int number;
    private int numberRest;

    private long userId;

    private int status;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    private boolean edit;

    public QualityControlWindowModel() {
    }

    public QualityControlWindowModel(long id, String barcode, String productSetName, int machineId, String violator, int qcId, int departmentId, long orderId, long productSetDetailId, String productSetDetailName, int totalNumber, int number, int numberRest, long userId, int status, boolean edit) {
        this.id = id;
        this.barcode = barcode;
        this.productSetName = productSetName;
        this.machineId = machineId;
        this.violator = violator;
        this.qcId = qcId;
        this.departmentId = departmentId;
        this.orderId = orderId;
        this.productSetDetailId = productSetDetailId;
        this.productSetDetailName = productSetDetailName;
        this.totalNumber = totalNumber;
        this.numberRest = numberRest;
        this.edit = edit;
        this.listReason = listReason;
        this.description = description;
        this.imageList = imageList;
        this.listImage = listImage;
        this.number = number;
        this.userId = userId;
        this.status = status;
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(QualityControlWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void create(Realm realm, int machineId, String violator, int qcId, ProductWindowEntity product, long userId) {
        QualityControlWindowModel qualityControlModel = new QualityControlWindowModel(id(realm) + 1, product.getBarcode(), product.getProductSetName(),
                machineId, violator, qcId,
                product.getDepartmentID(), product.getOrderId(), product.getProductSetDetailID(), product.getProductSetDetailName(), product.getNumberTotalOrder(),
                1, product.getNumberWaitting(), userId, Constants.WAITING_UPLOAD, false);
        realm.copyToRealm(qualityControlModel);
    }

    public static RealmResults<QualityControlWindowModel> getListQualityControl(Realm realm) {
        RealmResults<QualityControlWindowModel> results = realm.where(QualityControlWindowModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;

    }

    public static void updateDetailErrorQC(Realm realm, long id, int numberFailed, String description, Collection<Integer> idList) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        RealmList<Integer> integers = qualityControlModel.getIdReasonList();
        integers.clear();
        String sId = "";
        for (int idReason : idList) {
            integers.add(idReason);
            sId += idReason + ",";
        }
        qualityControlModel.setListReason(sId.substring(0, sId.lastIndexOf(",")));
        qualityControlModel.setDescription(description);
        qualityControlModel.setNumber(numberFailed);
        qualityControlModel.setEdit(true);

    }

    public static void updateListImage(Realm realm, long id, String filePath) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        RealmList<ImageModel> imageModels = qualityControlModel.getImageList();
        ImageModel imageModel = ImageModel.create(realm, filePath);
        imageModel = realm.copyToRealm(imageModel);
        imageModels.add(imageModel);
    }

    public static void updateStatusAndServerId(Realm realm, long id, long imageId, long serverId) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        ImageModel imageModel = qualityControlModel.getImageList().where().equalTo("id", imageId).findFirst();
        if (imageModel != null) {
            imageModel.setStatus(Constants.COMPLETE);
            imageModel.setServerId(serverId);
        }
        qualityControlModel.setListImage(qualityControlModel.getListImage().length() == 0 ? String.valueOf(serverId) : qualityControlModel.getListImage() + "," + serverId);

    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RealmList<Integer> getIdReasonList() {
        return idReasonList;
    }

    public void setIdReasonList(RealmList<Integer> idReasonList) {
        this.idReasonList = idReasonList;
    }

    public static QualityControlWindowModel getDetailQualityControl(Realm realm, long id) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        return qualityControlModel;
    }

    public static RealmList<Integer> getListReasonQualityControl(Realm realm, long id) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        RealmList<Integer> results = qualityControlModel.getIdReasonList();
        return results;
    }

    public static List<QualityControlWindowModel> getListQualityControlUpload(Realm realm) {
        RealmResults<QualityControlWindowModel> results = realm.where(QualityControlWindowModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("edit", true).findAll();
        return realm.copyFromRealm(results);
    }

    public static void updateImageIdAndStatus(Realm realm, long qcId, long id, long imageId) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", qcId).findFirst();
        ImageModel imageModel = qualityControlModel.getImageList().where().equalTo("id", id).findFirst();
        imageModel.setServerId(imageId);
        imageModel.setStatus(Constants.COMPLETE);
        qualityControlModel.setListImage(!TextUtils.isEmpty(qualityControlModel.getListImage()) ? qualityControlModel.getListImage() + "," + imageId : String.valueOf(imageId));

    }

    public static void updateStatusQC(Realm realm) {
        RealmResults<QualityControlWindowModel> results = realm.where(QualityControlWindowModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("edit", true).findAll();
        for (QualityControlWindowModel qualityControlModel : results) {
            qualityControlModel.setStatus(Constants.COMPLETE);
        }
    }

    public static void deleteQC(Realm realm, long id) {
        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("id", id).findFirst();
        qualityControlModel.deleteFromRealm();
    }

    public static boolean checkBarcodeExistInQC(Realm realm, String barcode) {

        QualityControlWindowModel qualityControlModel = realm.where(QualityControlWindowModel.class).equalTo("barcode", barcode)
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return qualityControlModel != null;
    }

    public int getMachineId() {
        return machineId;
    }

    public String getViolator() {
        return violator;
    }

    public int getQcId() {
        return qcId;
    }

    public long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductSetName() {
        return productSetName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public String getListReason() {
        return listReason;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public RealmList<ImageModel> getImageList() {
        return imageList;
    }

    public String getListImage() {
        return listImage;
    }

    public int getNumber() {
        return number;
    }

    public long getUserId() {
        return userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setProductSetName(String productSetName) {
        this.productSetName = productSetName;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public void setViolator(String violator) {
        this.violator = violator;
    }

    public void setQcId(int qcId) {
        this.qcId = qcId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setProductSetDetailId(long productSetDetailId) {
        this.productSetDetailId = productSetDetailId;
    }

    public void setProductSetDetailName(String productSetDetailName) {
        this.productSetDetailName = productSetDetailName;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public void setListReason(String listReason) {
        this.listReason = listReason;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageList(RealmList<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static void deleteAlLQC(Realm realm) {
        RealmResults<QualityControlWindowModel> results = realm.where(QualityControlWindowModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        results.deleteAllFromRealm();
    }
}
