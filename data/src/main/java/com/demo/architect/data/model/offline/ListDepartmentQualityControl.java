package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListDepartmentQualityControl extends RealmObject {
    @PrimaryKey
    private int departmentId;

    @SuppressWarnings("unused")
    private RealmList<QualityControlModel> list;

    public ListDepartmentQualityControl() {
    }

    public ListDepartmentQualityControl(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public RealmList<QualityControlModel> getList() {
        return list;
    }

    public void setList(RealmList<QualityControlModel> list) {
        this.list = list;
    }
    public static ListDepartmentQualityControl create(Realm realm, int departmentId) {
        ListDepartmentQualityControl listDepartmentQualityControl = new ListDepartmentQualityControl(departmentId);
        realm.beginTransaction();
        listDepartmentQualityControl = realm.copyToRealm(listDepartmentQualityControl);
        realm.commitTransaction();
        return listDepartmentQualityControl;
    }

}
