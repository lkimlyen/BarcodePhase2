package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListSerialPackPagkaging extends RealmObject {
    @PrimaryKey
    private int id;

    private String serialPack;
    private String codeProduct;
    @SuppressWarnings("unused")
    private RealmList<LogScanPackaging> list;

    public LogListSerialPackPagkaging() {
    }

    public LogListSerialPackPagkaging(int id, String serialPack, String codeProduct) {
        this.id = id;
        this.serialPack = serialPack;
        this.codeProduct = codeProduct;
    }


    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListSerialPackPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static LogListSerialPackPagkaging create(Realm realm, String codeProduct, String module) {
        LogListSerialPackPagkaging log = new LogListSerialPackPagkaging(id(realm) + 1, module, codeProduct);
        log = realm.copyToRealm(log);
        return log;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return serialPack;
    }

    public void setModule(String module) {
        this.serialPack = module;
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

    public static int getTotalScan(Realm realm,int logId) {
        LogListSerialPackPagkaging log = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("id",logId).findFirst();
        int   sum     = log.getList().sum("age").intValue();
        return sum;
    }
}
