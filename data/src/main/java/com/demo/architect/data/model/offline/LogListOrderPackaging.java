package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListOrderPackaging extends RealmObject {
    @PrimaryKey
    private int orderId;
    @SuppressWarnings("unused")
    private RealmList<LogListFloorPagkaging> list;

    public LogListOrderPackaging() {
    }

    public LogListOrderPackaging(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public RealmList<LogListFloorPagkaging> getList() {
        return list;
    }

    public static LogListOrderPackaging create(Realm realm, int orderId) {
        LogListOrderPackaging logListOrderPackaging = new LogListOrderPackaging(orderId);
        logListOrderPackaging = realm.copyToRealm(logListOrderPackaging);
        return logListOrderPackaging;
    }
}
