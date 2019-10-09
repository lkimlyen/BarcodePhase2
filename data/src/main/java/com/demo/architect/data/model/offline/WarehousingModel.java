package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class WarehousingModel extends RealmObject {

    @PrimaryKey
    private long id;
    private long orderId;

    @Expose
    @SerializedName("pProductID")
    private long productId;

    private ProductWarehouseModel productModel;

    @Expose
    @SerializedName("pBarCode")
    private String barcode;

    @Expose
    @SerializedName("pPack")
    private String pack;

    //sl gói
    @Expose
    @SerializedName("pNumber")
    private int number;
    //sl quét
    private int numberInput;

    private int numberPack;

    @Expose
    @SerializedName("pDateTimeScan")
    private String dateScan;

    @Expose
    @SerializedName("pLongGPS")
    private double longitude;
    @Expose
    @SerializedName("pLatGPS")
    private double latitude;
    private int status;

    private long userId;

    public WarehousingModel() {
    }

    public WarehousingModel(long orderId, long productId, String barcode, String pack, int number, int numberInput, int numberPack, String dateScan, double longitude, double latitude, int status, long userId) {
        this.orderId = orderId;
        this.productId = productId;
        this.barcode = barcode;
        this.pack = pack;
        this.number = number;
        this.numberInput = numberInput;
        this.numberPack = numberPack;
        this.dateScan = dateScan;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.userId = userId;
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(WarehousingModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static void add(Realm realm, WarehousingModel model) {

        ProductWarehouseModel productDetail = realm.where(ProductWarehouseModel.class)
                .equalTo("productId", model.getProductId())
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("userId", model.getUserId()).findFirst();

        WarehousingModel warehousingModel = realm.where(WarehousingModel.class).equalTo("barcode", model.getBarcode())
                .equalTo("status", Constants.WAITING_UPLOAD).findFirst();

        if (warehousingModel == null) {
            model.setId(id(realm) + 1);
            warehousingModel = realm.copyToRealm(model);
            warehousingModel.setProductModel(productDetail);
        } else {
            warehousingModel.setNumberInput(warehousingModel.getNumberInput() + model.getNumberInput());
            warehousingModel.setNumber(warehousingModel.getNumberInput() * warehousingModel.getNumberPack());
        }

        productDetail.setNumberScanned(productDetail.getNumberScanned() + (model.getNumberInput() * model.getNumberPack()));
        productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
    }

    public static void updateNumberInput(Realm realm, long id, int numberInput) {
        WarehousingModel warehousingModel = realm.where(WarehousingModel.class).equalTo("id", id).findFirst();
        int number = numberInput * warehousingModel.getNumberPack() - warehousingModel.getNumberInput() * warehousingModel.getNumberPack();
        warehousingModel.setNumberInput(numberInput);
        warehousingModel.setNumber(warehousingModel.getNumberInput() * warehousingModel.getNumberPack());
        ProductWarehouseModel productDetail = warehousingModel.getProductModel();
        productDetail.setNumberScanned(productDetail.getNumberScanned() + number);
        productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
    }

    public static void delete(Realm realm, long id) {
        WarehousingModel warehousingModel = realm.where(WarehousingModel.class).equalTo("id", id).findFirst();
        ProductWarehouseModel productDetail = warehousingModel.getProductModel();
        productDetail.setNumberScanned(productDetail.getNumberScanned() - (warehousingModel.getNumberInput() * warehousingModel.getNumberPack()));
        productDetail.setNumberRest(productDetail.getNumberTotal() - productDetail.getNumberScanned());
        warehousingModel.deleteFromRealm();

    }


    public static RealmResults<WarehousingModel> getAllList(Realm realm) {
        RealmResults<WarehousingModel> results = realm.where(WarehousingModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();
        return results;
    }

    public static void updateStatus(Realm realm) {
        RealmResults<WarehousingModel> results = realm.where(WarehousingModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD)
                .findAll();
        for (WarehousingModel model : results) {
            ProductWarehouseModel detailWindowModel = model.getProductModel();
            detailWindowModel.setStatus(Constants.COMPLETE);
            model.setStatus(Constants.COMPLETE);
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductModel(ProductWarehouseModel productModel) {
        this.productModel = productModel;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setNumberPack(int numberPack) {
        this.numberPack = numberPack;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public ProductWarehouseModel getProductModel() {
        return productModel;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPack() {
        return pack;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public int getNumberPack() {
        return numberPack;
    }

    public String getDateScan() {
        return dateScan;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getStatus() {
        return status;
    }

    public long getUserId() {
        return userId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
