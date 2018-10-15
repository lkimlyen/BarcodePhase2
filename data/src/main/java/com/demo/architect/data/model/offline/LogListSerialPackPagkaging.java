package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListSerialPackPagkaging extends RealmObject {
    @PrimaryKey
    private long id;
    private String serialPack;
    private String codeProduct;
    private long productId;
    private String module;
    private double numberTotal;
    private int size;
    @SuppressWarnings("unused")
    private RealmList<LogScanPackaging> list;

    public LogListSerialPackPagkaging() {
    }

    public LogListSerialPackPagkaging(long id, String serialPack, String codeProduct, long productId, double numberTotal, int size, String module) {
        this.id = id;
        this.serialPack = serialPack;
        this.codeProduct = codeProduct;
        this.productId = productId;
        this.numberTotal = numberTotal;
        this.size = size;
        this.module = module;
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogListSerialPackPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static LogListSerialPackPagkaging create(Realm realm, String serialPack, String codeProduct, long productId, double numberTotal, String module) {
        LogListSerialPackPagkaging log = new LogListSerialPackPagkaging(id(realm) + 1, serialPack, codeProduct, productId, numberTotal, 0, module);
        log = realm.copyToRealm(log);
        return log;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<LogScanPackaging> getList() {
        return list;
    }

    public void setList(RealmList<LogScanPackaging> list) {
        this.list = list;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public void setSerialPack(String serialPack) {
        this.serialPack = serialPack;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    @Override
    public String toString() {
        return serialPack;
    }

    public double getNumberTotal() {
        return numberTotal;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNumberTotal(double numberTotal) {
        this.numberTotal = numberTotal;
    }
}
