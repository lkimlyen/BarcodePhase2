package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackagingModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int productId;
    private String productName;
    private String productColor;
    private String serialPack;
    private int width;
    private int length;
    private int height;
    private int numberTotal;
    private int numberScan;
    private int numberRest;
    private int status;

    public ProductPackagingModel() {
    }

    public ProductPackagingModel(int id, int productId, String productName, String productColor, String serialPack, int width, int length, int height, int numberTotal, int numberScan, int numberRest, int status) {
        this.id = id;
        this.productId = productId;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public int getNumberScan() {
        return numberScan;
    }

    public void setNumberScan(int numberScan) {
        this.numberScan = numberScan;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }


    public static ProductPackagingModel findProductPackaging(Realm realm, int productId, String serialPack) {
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                .equalTo("productId", productId).equalTo("serialPack",serialPack).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return productPackagingModel;

    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public void setSerialPack(String serialPack) {
        this.serialPack = serialPack;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
