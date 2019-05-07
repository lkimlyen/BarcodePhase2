package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.ProductWindowEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductWarehouseModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long productId;
    private String productName;
    private String productCode;
    private String pack;
    private int numberTotal;
    private int numberSuccess;
    private int numberScanned;
    private int numberRest;
    private long userId;
    private int status;

    public ProductWarehouseModel() {
    }

    public ProductWarehouseModel(long id, long productId, String productName, String productCode, String pack, int numberTotal, int numberSuccess, int numberScanned, int numberRest, long userId, int status) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.pack = pack;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.userId = userId;
        this.status = status;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ProductWarehouseModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static ProductWarehouseModel create(Realm realm, ProductWarehouseEntity productEntity, long userId) {
        realm.beginTransaction();
        ProductWarehouseModel
                productDetail = new ProductWarehouseModel(id(realm) + 1, productEntity.getProductId(), productEntity.getProductName(),
                productEntity.getProductCode(), productEntity.getPack(), productEntity.getNumberTotal(), productEntity.getNumberSucess(),
                productEntity.getNumberSucess(),
                productEntity.getNumberWaiting(), userId, Constants.WAITING_UPLOAD);
        productDetail = realm.copyToRealm(productDetail);
        realm.commitTransaction();
        return productDetail;
    }

    public static ProductWarehouseModel getProductDetail(Realm realm, ProductWarehouseEntity productEntity, long userId) {
        ProductWarehouseModel productDetail =
                realm.where(ProductWarehouseModel.class).equalTo("productId", productEntity.getProductId())
                        .equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("userId", userId).findFirst();

        return productDetail;
    }


    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getPack() {
        return pack;
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

    public int getStatus() {
        return status;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
