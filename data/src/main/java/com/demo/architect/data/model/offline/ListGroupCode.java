package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListGroupCode extends RealmObject {
    @PrimaryKey
    private int id;
    private String groupCode;
    @SuppressWarnings("unused")
    private RealmList<LogScanStages> list;
    private String module;

    public ListGroupCode() {
    }
    public ListGroupCode(int id, String groupCode, String module) {
        this.id = id;
        this.groupCode = groupCode;
        this.module = module;
    }

    public RealmList<LogScanStages> getList() {
        return list;
    }

    public void setList(RealmList<LogScanStages> list) {
        this.list = list;
    }



    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ListGroupCode.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public int getId() {
        return id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return groupCode;
    }
}
