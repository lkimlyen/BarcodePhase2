package com.demo.architect.data.model;

public class PositionScan {
    private int orderId;
    private int apartmentId;
    private String module;
    private String serialPack;
    private String codePack;

    public PositionScan(int orderId, int apartmentId, String module, String serialPack, String codePack) {
        this.orderId = orderId;
        this.apartmentId = apartmentId;
        this.module = module;
        this.serialPack = serialPack;
        this.codePack = codePack;
    }

    public String getModule() {
        return module;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public String getCodePack() {
        return codePack;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public int getOrderId() {
        return orderId;
    }
}
