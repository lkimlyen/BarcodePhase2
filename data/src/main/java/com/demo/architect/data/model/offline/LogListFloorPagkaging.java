package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListFloorPagkaging extends RealmObject {
    @PrimaryKey
    private int id;
    private String floor;
    @SuppressWarnings("unused")
    private RealmList<LogListModulePagkaging> list;

    public LogListFloorPagkaging() {
    }

    public LogListFloorPagkaging(int id, String floor) {
        this.id = id;
        this.floor = floor;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public RealmList<LogListModulePagkaging> getList() {
        return list;
    }

    public void setList(RealmList<LogListModulePagkaging> list) {
        this.list = list;
    }

    public static LogListFloorPagkaging create(Realm realm, String floor) {
        LogListFloorPagkaging log = new LogListFloorPagkaging(id(realm) + 1, floor);
        log = realm.copyToRealm(log);
        return log;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListFloorPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }
}