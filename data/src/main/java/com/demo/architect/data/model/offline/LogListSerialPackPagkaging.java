package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.CodePackEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListSerialPackPagkaging extends RealmObject {
   @PrimaryKey
    private long id;

    private String serialPack;
    private String codeProduct;
    @SuppressWarnings("unused")
    private RealmList<LogScanPackaging> list;

    public LogListSerialPackPagkaging() {
    }

    public LogListSerialPackPagkaging(long id, String serialPack, String codeProduct) {
        this.id = id;
        this.serialPack = serialPack;
        this.codeProduct = codeProduct;
    }


    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(LogListSerialPackPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static LogListSerialPackPagkaging create(Realm realm, CodePackEntity codePackEntity) {
        LogListSerialPackPagkaging log = new LogListSerialPackPagkaging(id(realm) + 1, codePackEntity.getSttPack(), codePackEntity.getPackCode());
        log = realm.copyToRealm(log);
        return log;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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


}
