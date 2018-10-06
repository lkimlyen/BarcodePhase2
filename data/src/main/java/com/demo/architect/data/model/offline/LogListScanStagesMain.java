package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStagesMain extends RealmObject {
    @PrimaryKey
    private long orderId;
    @SuppressWarnings("unused")
    private RealmList<LogListScanStages> stagesList;

    @SuppressWarnings("unused")
    private RealmList<GroupCode> groupCodeRealmList;
    public LogListScanStagesMain() {
    }

    public LogListScanStagesMain(long orderId) {
        this.orderId = orderId;
    }

    public RealmList<LogListScanStages> getList() {
        return stagesList;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public RealmList<GroupCode> getGroupCodeRealmList() {
        return groupCodeRealmList;
    }

    public void setGroupCodeRealmList(RealmList<GroupCode> groupCodeRealmList) {
        this.groupCodeRealmList = groupCodeRealmList;
    }
}
