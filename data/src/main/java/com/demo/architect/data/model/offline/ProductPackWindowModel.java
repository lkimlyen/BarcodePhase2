package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductPackagingWindowEntity;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductPackWindowModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long productSetId;
    private long productSetDetailId;
    private String productSetDetailName;
    private String productSetDetailCode;
    private String barcode;
    private String codeOrigin;
    private double width;
    private double length;
    private double height;
    private int numberTotal;
    //sl đã quét đã tải lên server
    private int numberScaned;
    //sl đã quét
    private int numberScan;
    //sl còn lại
    private int numberRest;

    public ProductPackWindowModel() {
    }

    public ProductPackWindowModel(long id, long orderId, long productSetId, long productSetDetailId, String productSetDetailName,
                                  String productSetDetailCode, String barcode, String codeOrigin,  double width, double length, double height,
                                  int numberTotal, int numberScaned, int numberScan, int numberRest) {
        this.id = id;
        this.orderId = orderId;
        this.productSetId = productSetId;
        this.productSetDetailId = productSetDetailId;
        this.productSetDetailName = productSetDetailName;
        this.productSetDetailCode = productSetDetailCode;
        this.barcode = barcode;
        this.codeOrigin = codeOrigin;
        this.width = width;
        this.length = length;
        this.height = height;
        this.numberTotal = numberTotal;
        this.numberScaned = numberScaned;
        this.numberScan = numberScan;
        this.numberRest = numberRest;
    }

    public static ProductPackWindowModel findProductPackaging(Realm realm, long productSetDetailId, long productSetId) {
        ProductPackWindowModel productPackagingModel = realm.where(ProductPackWindowModel.class)
                .equalTo("productSetDetailId", productSetDetailId)
                .equalTo("productSetId", productSetId).findFirst();
        return productPackagingModel;

    }

    public static ProductPackWindowModel create(Realm realm, ProductPackagingWindowEntity entity) {
        realm.beginTransaction();
        ProductPackWindowModel productPackWindowModel = new ProductPackWindowModel(id(realm)+1,
                entity.getOrderId(),entity.getProductSetId(),entity.getProductSetDetailId(),
                entity.getProductSetDetailName(), entity.getProductSetDetailCode(),
                entity.getBarcode(),entity.getCodeOrigin(),entity.getWidth(),entity.getLength(),entity.getHeight(),
                entity.getNumberTotal(),entity.getNumberScaned(),0,entity.getNumberTotal()-entity.getNumberScaned());
        productPackWindowModel = realm.copyToRealm(productPackWindowModel);
        realm.commitTransaction();
        return productPackWindowModel;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ProductPackWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductSetId() {
        return productSetId;
    }

    public void setProductSetId(long productSetId) {
        this.productSetId = productSetId;
    }

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public void setProductSetDetailId(long productSetDetailId) {
        this.productSetDetailId = productSetDetailId;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public void setProductSetDetailName(String productSetDetailName) {
        this.productSetDetailName = productSetDetailName;
    }

    public String getProductSetDetailCode() {
        return productSetDetailCode;
    }

    public void setProductSetDetailCode(String productSetDetailCode) {
        this.productSetDetailCode = productSetDetailCode;
    }

    public String getCodeOrigin() {
        return codeOrigin;
    }

    public void setCodeOrigin(String codeOrigin) {
        this.codeOrigin = codeOrigin;
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

    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public int getNumberScaned() {
        return numberScaned;
    }

    public void setNumberScaned(int numberScaned) {
        this.numberScaned = numberScaned;
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

    public String getBarcode() {
        return barcode;
    }
}
