package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListOrderQualityControl extends RealmObject {
    @PrimaryKey
    private int orderId;
    @SuppressWarnings("unused")
    private RealmList<ListDepartmentQualityControl> list;

    public ListOrderQualityControl() {
    }

    public ListOrderQualityControl(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public RealmList<ListDepartmentQualityControl> getList() {
        return list;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setList(RealmList<ListDepartmentQualityControl> list) {
        this.list = list;
    }

    public static ListOrderQualityControl create(Realm realm, int orderId) {

        ListOrderQualityControl listOrderQualityControl = new ListOrderQualityControl(orderId);
        realm.beginTransaction();
        listOrderQualityControl = realm.copyToRealm(listOrderQualityControl);
        realm.commitTransaction();
        return listOrderQualityControl;
    }
}
