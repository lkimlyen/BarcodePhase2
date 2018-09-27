package com.demo.architect.data.model.offline;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStagesMain extends RealmObject {
    @PrimaryKey
    private long orderId;
    @SuppressWarnings("unused")
    private RealmList<LogListScanStages> stagesList;

    private RealmList<ListModule> listModule;
    public LogListScanStagesMain() {
    }

    public LogListScanStagesMain(long orderId) {
        this.orderId = orderId;
    }



    public RealmList<LogListScanStages> getList() {
        return stagesList;
    }


    public RealmList<ListModule> getListModule() {
        return listModule;
    }

    public void setListModule(RealmList<ListModule> listModule) {
        this.listModule = listModule;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
