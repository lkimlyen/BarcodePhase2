package com.demo.architect.data.model.offline;

import android.media.Image;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class QualityControlModel extends RealmObject {

    @PrimaryKey
    private int id;
    @SerializedName("pBarcode")
    @Expose
    private String barcode;
    private String module;

    @SerializedName("pDepartmentID")
    @Expose
    private int departmentId;

    @SerializedName("pOrderID")
    @Expose
    private int orderId;

    @SerializedName("pProductDetailID")
    @Expose
    private int productDetailId;
    private String productName;
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

    @SerializedName("pUserId")
    @Expose
    private int userId;

    private int status;

    public QualityControlModel() {
    }

    public QualityControlModel(int id, String barcode, String module, int departmentId, int orderId, int productDetailId, String productName, int totalNumber, int number, int userId, int status) {
        this.id = id;
        this.barcode = barcode;
        this.module = module;
        this.departmentId = departmentId;
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.productName = productName;
        this.totalNumber = totalNumber;
        this.listReason = listReason;
        this.description = description;
        this.imageList = imageList;
        this.listImage = listImage;
        this.number = number;
        this.userId = userId;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(QualityControlModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, int orderId, int departmentId, ProductEntity product, int userId) {

        ListOrderQualityControl listOrderQualityControl = realm.where(ListOrderQualityControl.class).equalTo("orderId", orderId).findFirst();
        ListDepartmentQualityControl listDepartmentQualityControl = listOrderQualityControl.getList().where().equalTo("departmentId", departmentId).findFirst();
        RealmList<QualityControlModel> parentList = listDepartmentQualityControl.getList();
        QualityControlModel qualityControlModel = new QualityControlModel(id(realm) + 1, product.getBarcode(), product.getModule(), departmentId,
                product.getOrderId(), product.getProductDetailID(), product.getProductDetailName(), product.getNumberTotalOrder(),
                1, userId, Constants.WAITING_UPLOAD);
        qualityControlModel = realm.copyToRealm(qualityControlModel);
        parentList.add(qualityControlModel);
    }

    public static RealmResults<QualityControlModel> getListQualityControl(Realm realm, int orderId, int departmentId, int userId) {

        ListOrderQualityControl listOrderQualityControl = realm.where(ListOrderQualityControl.class).equalTo("orderId", orderId).findFirst();
        if (listOrderQualityControl == null) {
            listOrderQualityControl = ListOrderQualityControl.create(realm, orderId);
        }

        RealmList<ListDepartmentQualityControl> parentList = listOrderQualityControl.getList();
        ListDepartmentQualityControl listDepartmentQualityControl = listOrderQualityControl.getList().where().equalTo("departmentId", departmentId).findFirst();
        if (listDepartmentQualityControl == null) {
            listDepartmentQualityControl = ListDepartmentQualityControl.create(realm, departmentId);
            parentList.add(listDepartmentQualityControl);
        }

        RealmResults<QualityControlModel> results = listDepartmentQualityControl.getList().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;

    }

    public static void updateNumber(Realm realm, int id, int number) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        qualityControlModel.setNumber(number);
    }

    public static void updateListReason(Realm realm, int id, Collection<Integer> idList) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        RealmList<Integer> integers = qualityControlModel.getIdReasonList();
        integers.clear();
        String sId = "";
        for (int idReason : idList) {
            integers.add(idReason);
            sId += idReason + ",";
        }
        qualityControlModel.setListReason(sId.substring(0, sId.lastIndexOf(",")));


    }

    public static void updateListImage(Realm realm, int id, String filePath) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        RealmList<ImageModel> imageModels = qualityControlModel.getImageList();
        ImageModel imageModel = ImageModel.create(realm, filePath);
        imageModel = realm.copyToRealm(imageModel);
        imageModels.add(imageModel);
    }

    public static void updateStatusAndServerId(Realm realm, int id, int imageId, int serverId) {
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

    public static QualityControlModel getDetailQualityControl(Realm realm, int id) {
        QualityControlModel qualityControlModel = realm.where(QualityControlModel.class).equalTo("id", id).findFirst();
        return qualityControlModel;
    }
}
