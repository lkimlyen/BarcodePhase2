package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStagesMain extends RealmObject {
    @PrimaryKey
    private int orderId;
    @SuppressWarnings("unused")
    private RealmList<LogListScanStages> stagesList;

    public LogListScanStagesMain() {
    }

    public LogListScanStagesMain(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public RealmList<LogListScanStages> getList() {
        return stagesList;
    }
}
