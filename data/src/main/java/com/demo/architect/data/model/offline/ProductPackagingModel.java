package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.Result;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackagingModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long productId;
    private long productDetailId;
    private String productName;
    private String productColor;
    private String serialPack;
    private double width;
    private double length;
    private double height;
    private int numberTotal;
    private int numberScan;
    private int numberRest;
    private int status;

    public ProductPackagingModel() {
    }

    public ProductPackagingModel(long id, long productId, long productDetailId, String productName, String productColor, String serialPack, double width, double length, double height, int numberTotal, int numberScan, int numberRest, int status) {
        this.id = id;
        this.productId = productId;
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


    public static ProductPackagingModel findProductPackaging(Realm realm, long productId, long productDetailId, String serialPack) {
        ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                .equalTo("productId", productId)
                .equalTo("productDetailId", productDetailId).equalTo("serialPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        return productPackagingModel;

    }

    public static Result findProductPackagingByList(Realm realm, List<Result> list) {

        Result result1 = null;
        for (Result result : list) {
            long productId = result.getListModuleEntity().getProductId();
            long producDetailId = result.getProductPackagingEntity().getId();
            String serialPack = result.getPackageEntity().getSerialPack();
            int numberScan = result.getPackageEntity().getNumberScan();
            int numberRequired = result.getListModuleEntity().getNumberRequired();
            ProductPackagingModel productPackagingModel = realm.where(ProductPackagingModel.class)
                    .equalTo("productId", productId)
                    .equalTo("productDetailId", producDetailId)
                    .equalTo("serialPack", serialPack).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
            if (numberScan < numberRequired) {
                if (productPackagingModel == null) {
                    result1 = result;
                    break;
                } else {
                    if (productPackagingModel.getNumberRest() > 0) {
                        result1 = result;
                        break;
                    }
                }
            }

        }

        return result1;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public void setLength(int length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public int getStatus() {
        return status;
    }
}
