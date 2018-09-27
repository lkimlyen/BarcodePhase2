package com.demo.architect.data.model;

public class PositionScan {
    private long orderId;
    private long apartmentId;
    private String module;
    private String serialPack;
    private String codePack;

    public PositionScan(long orderId, long apartmentId, String module, String serialPack, String codePack) {
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

    public long getApartmentId() {
        return apartmentId;
    }

    public long getOrderId() {
        return orderId;
    }
}
