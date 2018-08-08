package com.demo.architect.data.model.offline;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStagesMain extends RealmObject {
    @PrimaryKey
    private int orderId;
    private List<LogListScanStages> list;

    public LogListScanStagesMain() {
    }

    public LogListScanStagesMain(int orderId, List<LogListScanStages> list) {
        this.orderId = orderId;
        this.list = list;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<LogListScanStages> getList() {
        return list;
    }
}
