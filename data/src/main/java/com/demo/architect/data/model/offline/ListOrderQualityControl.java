package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListOrderQualityControl extends RealmObject {
    @PrimaryKey
    private long orderId;
    @SuppressWarnings("unused")
    private RealmList<ListDepartmentQualityControl> list;

    public ListOrderQualityControl() {
    }

    public ListOrderQualityControl(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public RealmList<ListDepartmentQualityControl> getList() {
        return list;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setList(RealmList<ListDepartmentQualityControl> list) {
        this.list = list;
    }

    public static ListOrderQualityControl create(Realm realm, long orderId) {

        ListOrderQualityControl listOrderQualityControl = new ListOrderQualityControl(orderId);
        listOrderQualityControl = realm.copyToRealm(listOrderQualityControl);
        return listOrderQualityControl;
    }
}
