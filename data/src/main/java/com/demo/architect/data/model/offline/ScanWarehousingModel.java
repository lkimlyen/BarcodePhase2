package com.demo.architect.data.model.offline;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScanWarehousingModel extends RealmObject{
    @PrimaryKey
    private int id;
    private String barcode;
    private String deviceTime;
    private String serverTime;
    private double latitude;
    private double longitude;
    private String createByPhone;
    private int packageId;
    private int orderId;
    private int serial;
    private int createBy;

    public ScanWarehousingModel() {
    }

    public ScanWarehousingModel(int id, String barcode, String deviceTime, String serverTime, double latitude, double longitude, String createByPhone, int packageId, int orderId, int serial, int createBy) {
        this.id = id;
        this.barcode = barcode;
        this.deviceTime = deviceTime;
        this.serverTime = serverTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createByPhone = createByPhone;
        this.packageId = packageId;
        this.orderId = orderId;
        this.serial = serial;
        this.createBy = createBy;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreateByPhone() {
        return createByPhone;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getSerial() {
        return serial;
    }

    public int getCreateBy() {
        return createBy;
    }
}
