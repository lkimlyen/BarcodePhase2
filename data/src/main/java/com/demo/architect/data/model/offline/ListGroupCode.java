package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListGroupCode extends RealmObject {
   @PrimaryKey
    private long id;
    private String groupCode;
    @SuppressWarnings("unused")
    private RealmList<GroupCode> list;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String module;
    private double number;
    private int status;

    public ListGroupCode() {
    }

    public ListGroupCode(long id, String groupCode, String module, double number, int status) {
        this.id = id;
        this.groupCode = groupCode;
        this.module = module;
        this.number = number;
        this.status = status;
    }

    public RealmList<GroupCode> getList() {
        return list;
    }

    public void setList(RealmList<GroupCode> list) {
        this.list = list;
    }


    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(ListGroupCode.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    @Override
    public String toString() {
        return groupCode;
    }
}
