package com.demo.architect.data.model.offline;

import android.text.TextUtils;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class QualityControlModel extends RealmObject {

   @PrimaryKey
    private long id;
    @SerializedName("pBarcode")
    @Expose
    private String barcode;
    private String module;

    @SerializedName("pDepartmentID")
    @Expose
    private int departmentId;

    @SerializedName("pOrderID")
    @Expose
    private long orderId;

    @SerializedName("pProductDetailID")
    @Expose
    private long productDetailId;
    private String productName;
    private double totalNumber;

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
    private double number;

    @SerializedName("pUserId")
    @Expose
    private long userId;

    private int status;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    private boolean edit;

    public QualityControlModel() {
    }

    public QualityControlModel(long id, String barcode, String module, int departmentId, long orderId, long productDetailId, String productName, double totalNumber, double number, long userId, int status, boolean edit) {
        this.id = id;
        this.barcode = barcode;
        this.module = module;
        this.departmentId = departmentId;
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.productName = productName;
        this.totalNumber = totalNumber;
        this.edit = edit;
        this.listReason = listReason;
        this.description = description;
        this.imageList = imageList;
        this.listImage = listImage;
        this.number = number;
        this.userId = userId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(double totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getListReason() {
        return listReason;
    }

    public void setListReason(String listReason) {
        this.listReason = listReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<ImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(RealmList<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(QualityControlModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void create(Realm realm, long orderId, int departmentId, ProductEntity product, long userId) {

        ListOrderQualityControl listOrderQualityControl = realm.where(ListOrderQualityControl.class).equalTo("orderId", orderId).findFirst();
        ListDepartmentQualityControl listDepartmentQualityControl = listOrderQualityControl.getList().where().equalTo("departmentId", departmentId).findFirst();
        RealmList<QualityControlModel> parentList = listDepartmentQualityControl.getList();
        QualityControlModel qualityControlModel = new QualityControlModel(id(realm) + 1, product.getBarcode(), product.getModule(), departmentId,
                product.getOrderId(), product.getProductDetailID(), product.getProductDetailName(), product.getNumberTotalOrder(),
                1, userId, Constants.WAITING_UPLOAD, false);
        qualityControlModel = realm.copyToRealm(qualityControlModel);
        parentList.add(qualityControlModel);
    }

    public static RealmResults<QualityControlModel> getListQualityControl(Realm realm, long orderId, int departmentId, long userId) {

        ListOrderQualityControl listOrderQualityControl = realm.where(ListOrderQualityControl.class).equalTo("orderId", orderId).findFirst();
        if (listOrderQualityControl == null) {
            realm.beginTransaction();
            listOrderQualityControl = ListOrderQualityControl.create(realm, orderId);
            realm.commitTransaction();
        }

        RealmList<ListDepartmentQualityControl> parentList = listOrderQualityControl.getList();
        ListDepartmentQualityControl listDepartmentQualityControl = listOrderQualityControl.getList().where().equalTo("departmentId", departmentId).findFirst();
        if (listDepartmentQualityControl == null) {
            realm.beginTransaction();
            listDepartmentQualityControl = ListDepartmentQualityControl.create(realm, departmentId);
            parentList.add(listDepartmentQualityControl);
            realm.commitTransaction();
        }

        RealmResults<QualityControlModel> results = listDepartmentQualityControl.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;

    }

    public static void updateDetailErrorQC(Realm realm, long id, double numberFailed, String description, Collection<Integer> idList) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
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
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        RealmList<ImageModel> imageModels = qualityControlModel.getImageList();
        ImageModel imageModel = ImageModel.create(realm, filePath);
        imageModel = realm.copyToRealm(imageModel);
        imageModels.add(imageModel);
    }

    public static void updateStatusAndServerId(Realm realm, long id, long imageId, long serverId) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
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

    public static QualityControlModel getDetailQualityControl(Realm realm, long id) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        return qualityControlModel;
    }

    public static RealmList<Integer> getListReasonQualityControl(Realm realm, long id) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        RealmList<Integer> results = qualityControlModel.getIdReasonList();
        return results;
    }

    public static List<QualityControlModel> getListQualityControlUpload(Realm realm, long userId) {
        RealmResults<QualityControlModel> results = realm.where(QualityControlModel.class).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).equalTo("edit", true).findAll();
        return realm.copyFromRealm(results);
    }

    public static void updateImageIdAndStatus(Realm realm, long qcId, long id, long imageId) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", qcId).findFirst();
        ImageModel imageModel = qualityControlModel.getImageList().where().equalTo("id", id).findFirst();
        imageModel.setServerId(imageId);
        imageModel.setStatus(Constants.COMPLETE);
        qualityControlModel.setListImage(!TextUtils.isEmpty(qualityControlModel.getListImage()) ? qualityControlModel.getListImage() + "," + imageId : String.valueOf(imageId));

    }

    public static void updateStatusQC(Realm realm, long userId) {
        RealmResults<QualityControlModel> results = realm.where(QualityControlModel.class).equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("edit",true)
                .equalTo("userId", userId).findAll();
        for (QualityControlModel qualityControlModel : results) {
            qualityControlModel.setStatus(Constants.COMPLETE);
        }
    }
}
