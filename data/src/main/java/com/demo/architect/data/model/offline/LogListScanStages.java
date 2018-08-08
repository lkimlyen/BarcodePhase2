package com.demo.architect.data.model.offline;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStages extends RealmObject {
    @PrimaryKey
    private int id;
    private int department;
    private String date;
    private List<LogScanStages> list;

    public LogListScanStages() {
    }

    public LogListScanStages(int id, int department, String date, List<LogScanStages> list) {
        this.id = id;
        this.department = department;
        this.date = date;
        this.list = list;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public int getId() {
        return id;
    }

    public int getDepartment() {
        return department;
    }

    public String getDate() {
        return date;
    }

    public List<LogScanStages> getList() {
        return list;
    }
}
