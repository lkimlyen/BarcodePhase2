package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.GroupEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class GroupScan extends RealmObject {


    @PrimaryKey
    @SerializedName("pMasterGroupID")
    @Expose
    private long masterGroupId;

    @SerializedName("pVersion")
    @Expose
    private int version;
    private long orderId;

    public GroupScan() {
    }

    public GroupScan(long masterGroupId, int version, long orderId) {
        this.masterGroupId = masterGroupId;
        this.version = version;
        this.orderId = orderId;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(GroupScan.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void create(Realm realm, List<GroupEntity> list) {
        RealmResults<GroupScan> results = realm.where(GroupScan.class).findAll();
        results.deleteAllFromRealm();
        for (GroupEntity groupEntity : list){
            GroupScan groupScan = new GroupScan( groupEntity.getMasterGroupId(), groupEntity.getRowVersion(), groupEntity.getOrderId());
            realm.insertOrUpdate(groupScan);
        }

    }

    public static List<GroupScan> getListGroupScanVersion(Realm realm) {
        List<GroupScan> list = new ArrayList<>();

        RealmResults<LogScanStages> results = realm.where(LogScanStages.class).equalTo("status",Constants.WAITING_UPLOAD).findAll();

        for (LogScanStages logScanStages : results) {
            GroupScan groupScan = realm.where(GroupScan.class).equalTo("masterGroupId", logScanStages.getMasterGroupId()).findFirst();
            if (groupScan != null){
                list.add(realm.copyFromRealm(groupScan));
            }
        }

        return list;
    }

    public long getMasterGroupId() {
        return masterGroupId;
    }

    public void setMasterGroupId(long masterGroupId) {
        this.masterGroupId = masterGroupId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }




}
