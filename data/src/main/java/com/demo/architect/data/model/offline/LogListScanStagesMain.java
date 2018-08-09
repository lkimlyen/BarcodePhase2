package com.demo.architect.data.model.offline;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStagesMain extends RealmObject {
    @PrimaryKey
    private int orderId;
    @SuppressWarnings("unused")
    private RealmList<LogListScanStages> list;

    public LogListScanStagesMain() {
    }

    public LogListScanStagesMain(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public RealmList<LogListScanStages> getList() {
        return list;
    }
}
