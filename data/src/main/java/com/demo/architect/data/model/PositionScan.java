package com.demo.architect.data.model;

public class PositionScan {
    private String module;
    private String serialPack;
    private String codePack;

    public PositionScan( String module, String serialPack, String codePack) {

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
}
