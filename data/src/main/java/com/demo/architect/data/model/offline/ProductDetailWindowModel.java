package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductWindowEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductDetailWindowModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long productSetDetailID;
    private String productSetDetailName;
    private String productSetDetailCode;
    private String barcode;
    private String productSetName;
    private int numberTotal;
    private int numberSuccess;
    private int numberScanned;
    private int numberRest;
    private long userId;
    private RealmList<Integer> listStages;


    public ProductDetailWindowModel() {
    }

    public ProductDetailWindowModel(long id, long orderId, long productSetDetailID, String productSetDetailName,
                                    String ProductSetDetailCode, String barcode, String productSetName,
                                    int numberTotal, int numberSuccess, int numberScanned, int numberRest, long userId) {
        this.id = id;
        this.orderId = orderId;
        this.productSetDetailID = productSetDetailID;
        this.productSetDetailName = productSetDetailName;
        this.productSetDetailCode = ProductSetDetailCode;
        this.barcode = barcode;
        this.productSetName = productSetName;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.userId = userId;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public String getProductSetDetailCode() {
        return productSetDetailCode;
    }

    public RealmList<Integer> getListStages() {
        return listStages;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ProductDetailWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public void setProductSetDetailID(long productSetDetailID) {
        this.productSetDetailID = productSetDetailID;
    }

    public void setProductSetDetailName(String productSetDetailName) {
        this.productSetDetailName = productSetDetailName;
    }

    public void setProductSetDetailCode(String productSetDetailCode) {
        this.productSetDetailCode = productSetDetailCode;
    }

    public long getProductSetDetailID() {
        return productSetDetailID;
    }

    public static ProductDetailWindowModel create(Realm realm, ProductWindowEntity productEntity,  long userId) {
        realm.beginTransaction();
        ProductDetailWindowModel
                productDetail = new ProductDetailWindowModel(id(realm) + 1, productEntity.getOrderId(), productEntity.getProductSetDetailID(),
                productEntity.getProductSetDetailName(), productEntity.getProductSetDetailName(), productEntity.getBarcode(), productEntity.getProductSetName(), productEntity.getNumberTotalInput(),
                productEntity.getNumberSuccess(), productEntity.getNumberSuccess(), productEntity.getNumberWaitting(), userId);
        productDetail = realm.copyToRealm(productDetail);
        RealmList<Integer> listStages = productDetail.getListStages();
        for (Integer id : productEntity.getListDepartmentID()) {
            listStages.add(id);
        }

        realm.commitTransaction();
        return productDetail;
    }

    public static ProductDetailWindowModel getProductDetail(Realm realm, ProductWindowEntity productEntity, long userId) {
        ProductDetailWindowModel productDetail = realm.where(ProductDetailWindowModel.class).equalTo("productSetDetailID", productEntity.getProductSetDetailID())
                .equalTo("userId", userId).findFirst();

        return productDetail;
    }



    public long getId() {
        return id;
    }


    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public int getNumberScanned() {
        return numberScanned;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public long getUserId() {
        return userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductSetName() {
        return productSetName;
    }

    public void setNumberSuccess(int numberSuccess) {
        this.numberSuccess = numberSuccess;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }
}
