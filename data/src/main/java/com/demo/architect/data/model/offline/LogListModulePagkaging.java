package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListModulePagkaging extends RealmObject {
   @PrimaryKey
    private long id;
    private String module;
    private double size;

    @SuppressWarnings("unused")
    private RealmList<LogListSerialPackPagkaging> logScanPackagingList;


    public LogListModulePagkaging() {
    }

    public LogListModulePagkaging(long id, String module, double size) {
        this.id = id;
        this.module = module;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(LogListModulePagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static LogListModulePagkaging create(Realm realm, String module, long productId) {
        LogListModulePagkaging log = new LogListModulePagkaging(productId, module, 0);
        log = realm.copyToRealm(log);
        return log;
    }

    public RealmList<LogListSerialPackPagkaging> getLogScanPackagingList() {
        return logScanPackagingList;
    }

    public void setLogScanPackagingList(RealmList<LogListSerialPackPagkaging> logScanPackagingList) {
        this.logScanPackagingList = logScanPackagingList;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
