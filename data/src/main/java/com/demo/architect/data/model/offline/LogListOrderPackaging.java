package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListOrderPackaging extends RealmObject {
    @PrimaryKey
    private long orderId;
    private String codeSO;
    private String customerName;
    @SuppressWarnings("unused")
    private RealmList<LogListFloorPagkaging> list;

    public LogListOrderPackaging() {
    }

    public LogListOrderPackaging(long orderId, String codeSO, String customerName) {
        this.orderId = orderId;
        this.codeSO = codeSO;
        this.customerName = customerName;
    }

    public long getOrderId() {
        return orderId;
    }

    public RealmList<LogListFloorPagkaging> getList() {
        return list;
    }

    public static LogListOrderPackaging create(Realm realm, long orderId, String codeSO, String customerName) {
        LogListOrderPackaging logListOrderPackaging = new LogListOrderPackaging(orderId, codeSO, customerName);
        logListOrderPackaging = realm.copyToRealm(logListOrderPackaging);
        return logListOrderPackaging;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCodeSO() {
        return codeSO;
    }

    public void setCodeSO(String codeSO) {
        this.codeSO = codeSO;
    }

    public static LogListOrderPackaging findOrderPackaging(Realm realm, long orderId) {
        LogListOrderPackaging logListOrderPackaging = realm.where(LogListOrderPackaging.class)
                .equalTo("orderId", orderId).findFirst();
        return logListOrderPackaging;
    }
}
