package com.demo.architect.data.model.offline;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackagingModel extends RealmObject {
    @PrimaryKey
    private int productId;
    private String productName;
    private String productCode;
    private int numberTotal;
    private int numberSuccess;
    private int numberScan;
    private int numberRest;

    public ProductPackagingModel() {
    }

    public ProductPackagingModel(int productId, String productName, String productCode, int numberTotal, int numberSuccess, int numberScan, int numberRest) {
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public void setNumberSuccess(int numberSuccess) {
        this.numberSuccess = numberSuccess;
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
}
