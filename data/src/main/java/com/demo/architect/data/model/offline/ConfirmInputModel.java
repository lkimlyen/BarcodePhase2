package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ConfirmInputModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int orderId;
    private int departmentIDIn;
    private int departmentIDOut;
    private String productDetailName;
    private String module;
    private String barcode;
    private int numberTotalOrder;
    private int timesOutput;
    @SuppressWarnings("unused")
    private RealmList<NumberInputConfirmModel> listInputConfirmed;
    private int numberOut;
    private String dateTimeScan;
    @SuppressWarnings("unused")
    private RealmList<LogScanConfirm> listScanConfirm;

    public ConfirmInputModel() {

    }

    public ConfirmInputModel(int id, int orderId, int departmentIDIn, int departmentIDOut, String productDetailName, String module, String barcode,
                             int numberTotalOrder, int timesOutput, int numberOut, String dateTimeScan) {
        this.id = id;
        this.orderId = orderId;
        this.departmentIDIn = departmentIDIn;
        this.departmentIDOut = departmentIDOut;
        this.productDetailName = productDetailName;
        this.module = module;
        this.barcode = barcode;
        this.numberTotalOrder = numberTotalOrder;
        this.timesOutput = timesOutput;
        this.numberOut = numberOut;
        this.dateTimeScan = dateTimeScan;
    }

    public static void create(Realm realm, OrderConfirmEntity orderConfirmEntity) {
        ConfirmInputModel parent = realm.where(ConfirmInputModel.class).equalTo("id", orderConfirmEntity.getProductDetailID()).findFirst();
        if (parent == null) {
            parent = new ConfirmInputModel(orderConfirmEntity.getProductDetailID(), orderConfirmEntity.getOrderId(),
                    orderConfirmEntity.getDepartmentIDIn(), orderConfirmEntity.getDepartmentIDOut(), orderConfirmEntity.getProductDetailName(),
                    orderConfirmEntity.getModule(), orderConfirmEntity.getBarcode(), orderConfirmEntity.getNumberTotalOrder(),
                    orderConfirmEntity.getTimesOutput(), orderConfirmEntity.getNumberOut(), orderConfirmEntity.getDateTimeScan());
            parent = realm.copyToRealm(parent);
        }
        RealmList<NumberInputConfirmModel> parentList = parent.getListInputConfirmed();
        for (NumberInputConfirm numberInputConfirm : orderConfirmEntity.getListInputConfirmed()) {
            parentList.add(NumberInputConfirmModel.create(realm, numberInputConfirm));
        }

    }


    public static RealmResults<ConfirmInputModel> getListScanConfirm(Realm realm, int times) {
        RealmResults<NumberInputConfirm>
        RealmResults<ConfirmInputModel> results = realm.where(ConfirmInputModel.class).findAll();
        return results;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getDepartmentIDIn() {
        return departmentIDIn;
    }

    public int getDepartmentIDOut() {
        return departmentIDOut;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public String getModule() {
        return module;
    }

    public String getBarcode() {
        return barcode;
    }

    public int getNumberTotalOrder() {
        return numberTotalOrder;
    }

    public int getTimesOutput() {
        return timesOutput;
    }

    public RealmList<NumberInputConfirmModel> getListInputConfirmed() {
        return listInputConfirmed;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public String getDateTimeScan() {
        return dateTimeScan;
    }

    public RealmList<LogScanConfirm> getListScanConfirm() {
        return listScanConfirm;
    }
}
