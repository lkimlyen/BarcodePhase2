package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListModulePagkaging extends RealmObject {
    @PrimaryKey
    private int id;
    private String module;

    @SuppressWarnings("unused")
    private RealmList<LogScanPackaging> logScanPackagingList;


    public LogListModulePagkaging() {
    }

    public LogListModulePagkaging(int id, String module) {
        this.id = id;
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListModulePagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static LogListModulePagkaging create(Realm realm, String module, int productId) {
        LogListModulePagkaging log = new LogListModulePagkaging(productId, module);
        log = realm.copyToRealm(log);
        return log;
    }

    public RealmList<LogScanPackaging> getLogScanPackagingList() {
        return logScanPackagingList;
    }

    public void setLogScanPackagingList(RealmList<LogScanPackaging> logScanPackagingList) {
        this.logScanPackagingList = logScanPackagingList;
    }


}
