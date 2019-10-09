package com.demo.architect.data.model;

public class PositionScanWindow {
    private String packCode;
    private int numberPack;

    public PositionScanWindow(String packCode, int numberPack) {
        this.packCode = packCode;
        this.numberPack = numberPack;
    }

    public String getPackCode() {
        return packCode;
    }

    public int getNumberPack() {
        return numberPack;
    }
}
