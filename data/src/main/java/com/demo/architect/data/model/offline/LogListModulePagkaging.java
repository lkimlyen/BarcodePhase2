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
    private RealmList<LogListSerialPackPagkaging> list;

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

    public RealmList<LogListSerialPackPagkaging> getList() {
        return list;
    }

    public void setList(RealmList<LogListSerialPackPagkaging> list) {
        this.list = list;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListModulePagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static LogListModulePagkaging create(Realm realm, ModuleEntity moduleEntity) {
        LogListModulePagkaging log = new LogListModulePagkaging(moduleEntity.getProductId(),moduleEntity.getModuleName());
        realm.copyToRealm(log);
        return log;
    }


}
