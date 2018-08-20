package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanPagkaging extends RealmObject {
    @PrimaryKey
    private int id;
    private String serialPack;
    @SuppressWarnings("unused")
    private RealmList<LogScanPagkaging> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public void setSerialPack(String serialPack) {
        this.serialPack = serialPack;
    }

    public RealmList<LogScanPagkaging> getList() {
        return list;
    }

    public void setList(RealmList<LogScanPagkaging> list) {
        this.list = list;
    }
}
