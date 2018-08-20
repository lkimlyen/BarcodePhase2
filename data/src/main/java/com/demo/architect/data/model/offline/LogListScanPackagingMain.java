package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanPackagingMain extends RealmObject{
    @PrimaryKey
    private int orderId;
    private String module;
    @SuppressWarnings("unused")
    private RealmList<LogListScanPagkaging> list;

    public int getOrderId() {
        return orderId;
    }

    public String getModule() {
        return module;
    }

    public RealmList<LogListScanPagkaging> getList() {
        return list;
    }
}
