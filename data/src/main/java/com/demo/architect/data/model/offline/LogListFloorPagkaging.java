package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.ApartmentEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListFloorPagkaging extends RealmObject {
   @PrimaryKey
    private long id;
    private String floor;
    @SuppressWarnings("unused")
    private RealmList<LogListModulePagkaging> list;

    public LogListFloorPagkaging() {
    }

    public LogListFloorPagkaging(long id, String floor) {
        this.id = id;
        this.floor = floor;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public static LogListFloorPagkaging create(Realm realm, ApartmentEntity apartmentEntity) {
        LogListFloorPagkaging log = new LogListFloorPagkaging(apartmentEntity.getApartmentID(),apartmentEntity.getApartmentName());
        log = realm.copyToRealm(log);
        return log;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(LogListFloorPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }
}
