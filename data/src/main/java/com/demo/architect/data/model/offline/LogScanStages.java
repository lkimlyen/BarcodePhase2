package com.demo.architect.data.model.offline;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanStages extends RealmObject {

    @PrimaryKey
    private int id;
    private String barcode;
    private String module;
    private String nameProduct;
    private int numberTotal;
    private int numberScanned;
    private int numberRest;
    private int numberInput;
    private int times;
    private String dateScan;

    public LogScanStages() {
    }

    public LogScanStages(String barcode, String module, String nameProduct, int numberTotal, int numberScanned, int numberInput, int times) {
        this.id = id;
        this.barcode = barcode;
        this.module = module;
        this.nameProduct = nameProduct;
        this.numberTotal = numberTotal;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.numberInput = numberInput;
        this.times = times;
        this.dateScan = dateScan;
    }


    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getModule() {
        return module;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberScanned() {
        return numberScanned;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public int getTimes() {
        return times;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStages scanStages, int orderId, int departmentId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (mainParent == null) {
            mainParent = new LogListScanStagesMain(orderId);
            mainParent = realm.copyToRealm(mainParent);
        }

        RealmList<LogListScanStages> mainParentList = mainParent.getList();

        LogListScanStages parent = realm.where(LogListScanStages.class).equalTo("departmentId", departmentId)
                .equalTo("date", DateUtils.getShortDateCurrent()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        if (parent == null) {
            parent = new LogListScanStages(LogListScanStages.id(realm) + 1, departmentId, Constants.WAITING_UPLOAD, DateUtils.getShortDateCurrent());
            parent = realm.copyToRealm(parent);
            mainParentList.add(parent);
        }


    }


}
