package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStages extends RealmObject {
    @PrimaryKey
    private int id;
    private int departmentId;
    private int status;
    private String date;

    public RealmList<LogScanStages> getList() {
        return list;
    }

    @SuppressWarnings("unused")
    private RealmList<LogScanStages> list;

    public LogListScanStages() {
    }

    public LogListScanStages(int id, int departmentId, int status, String date) {
        this.id = id;
        this.departmentId = departmentId;
        this.status = status;
        this.date = date;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static int countDetailWaitingUpload(Realm realm, int orderId, int departmentId) {
        int count = 0;
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (logListScanStagesMain != null) {
            LogListScanStages logListScanStages = logListScanStagesMain.getList().where().equalTo("departmentId", departmentId)
                    .equalTo("status", Constants.WAITING_UPLOAD).equalTo("date", DateUtils.getShortDateCurrent()).findFirst();
            if (logListScanStages != null) {
                count = logListScanStages.getList().size();
            }
        }
        return count;
    }

    public int getId() {
        return id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }
}
