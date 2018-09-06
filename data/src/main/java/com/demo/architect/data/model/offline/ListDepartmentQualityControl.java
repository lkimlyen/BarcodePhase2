package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListDepartmentQualityControl extends RealmObject {
    @PrimaryKey
    private int id;
    private int departmentId;

    @SuppressWarnings("unused")
    private RealmList<QualityControlModel> list;

    public ListDepartmentQualityControl() {
    }

    public ListDepartmentQualityControl(int id, int departmentId) {
        this.id = id;
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
    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ListDepartmentQualityControl.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }
    public static ListDepartmentQualityControl create(Realm realm, int departmentId) {
        ListDepartmentQualityControl listDepartmentQualityControl = new ListDepartmentQualityControl(id(realm)+1, departmentId);
        listDepartmentQualityControl = realm.copyToRealm(listDepartmentQualityControl);

        return listDepartmentQualityControl;
    }

}
