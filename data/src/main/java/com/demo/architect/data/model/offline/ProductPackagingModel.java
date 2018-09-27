package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackagingModel extends RealmObject {
   @PrimaryKey
    private long id;
    private long productDetailId;
    private String productName;
    private String productColor;
    private String serialPack;
    private double width;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public void setSerialPack(String serialPack) {
        this.serialPack = serialPack;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(double numberTotal) {
        this.numberTotal = numberTotal;
    }

    public double getNumberScan() {
        return numberScan;
    }

    public void setNumberScan(double numberScan) {
        this.numberScan = numberScan;
    }

    public double getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(double numberRest) {
        this.numberRest = numberRest;
    }

    public int getStatus() {
        return status;
    }

    private double length;
    private double height;
    private double numberTotal;
    private double numberScan;
    private double numberRest;
    private int status;

    public ProductPackagingModel() {
    }

    public ProductPackagingModel(long id, long productDetailId, String productName, String productColor, String serialPack, double width, double length, double height, double numberTotal, double numberScan, double numberRest, int status) {
        this.id = id;
        this.productDetailId = productDetailId;
        this.productName = productName;
        this.productColor = productColor;
        this.serialPack = serialPack;
        this.width = width;
        this.length = length;
        this.height = height;
        this.numberTotal = numberTotal;
        this.numberScan = numberScan;
        this.numberRest = numberRest;
        this.status = status;
    }


    public static ProductPackagingModel findProductPackaging(Realm realm, long productId, String serialPack) {
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                .equalTo("productDetailId", productId).equalTo("serialPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return productPackagingModel;

    }

    public String getProductColor() {
        return productColor;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
