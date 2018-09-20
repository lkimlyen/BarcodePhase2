package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.GroupEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GroupScan extends RealmObject {
    @PrimaryKey
    @SerializedName("pMasterGroupID")
    @Expose
    private int masterGroupId;

    @SerializedName("pVersion")
    @Expose
    private int version;

    private String module;
    private int orderId;

    public GroupScan() {
    }

    public GroupScan(int masterGroupId, int version, String module, int orderId) {
        this.masterGroupId = masterGroupId;
        this.version = version;
        this.module = module;
        this.orderId = orderId;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(GroupScan.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, List<GroupEntity> list) {
        for (GroupEntity groupEntity : list){
            GroupScan groupScan = new GroupScan( groupEntity.getMasterGroupId(), groupEntity.getRowVersion(), groupEntity.getModule(), groupEntity.getOrderId());
            realm.insertOrUpdate(groupScan);
        }

    }


    public void setVersion(int version) {
        this.version = version;
    }

    public int getMasterGroupId() {
        return masterGroupId;
    }

    public void setMasterGroupId(int masterGroupId) {
        this.masterGroupId = masterGroupId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}
