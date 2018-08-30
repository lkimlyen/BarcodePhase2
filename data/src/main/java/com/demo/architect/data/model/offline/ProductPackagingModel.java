package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackagingModel extends RealmObject {
    @PrimaryKey
    private int productId;
    private String productName;
    private String productColor;

    private int width;
    private int length;
    private int height;
    private int numberTotal;
    private int numberScan;
    private int numberRest;

    public ProductPackagingModel() {
    }

    public ProductPackagingModel(int productId, String productName, String productColor, int width, int length, int height, int numberTotal, int numberScan, int numberRest) {
        this.productId = productId;
        this.productName = productName;
        this.productColor = productColor;
        this.width = width;
        this.length = length;
        this.height = height;
        this.numberTotal = numberTotal;
        this.numberScan = numberScan;
        this.numberRest = numberRest;
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


    public static ProductPackagingModel findProductPackaging(Realm realm, int productId) {
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                .equalTo("productId", productId).findFirst();
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

}
