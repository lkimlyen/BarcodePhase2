package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListScanStages extends RealmObject {
    @PrimaryKey
    private int id;
    private int departmentId;
    private int status;
    private int userId;
    private String date;

    public RealmList<LogScanStages> getList() {
        return list;
    }

    @SuppressWarnings("unused")
    private RealmList<LogScanStages> list;

    public LogListScanStages() {
    }

    public LogListScanStages(int id, int departmentId, int status, int userId, String date) {
        this.id = id;
        this.departmentId = departmentId;
        this.status = status;
        this.userId = userId;
        this.date = date;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static int countDetailWaitingUpload(Realm realm, int orderId, int departmentId, int userId) {
        int count = 0;
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (logListScanStagesMain != null) {
            LogListScanStages logListScanStages = logListScanStagesMain.getList().where().equalTo("departmentId", departmentId)
                    .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).equalTo("date", DateUtils.getShortDateCurrent()).findFirst();
            if (logListScanStages != null) {
                count = logListScanStages.getList().size();
            }
        }
        return count;
    }

    public static List<LogScanStages> getListScanStagesWaitingUpload(Realm realm, int orderId, int userId) {
        List<LogScanStages> list = new ArrayList<>();
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (logListScanStagesMain != null) {
            LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                    .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).findFirst();
            if (logListScanStages != null) {
                list = realm.copyFromRealm(logListScanStages.getList());
            }
        }
        return list;
    }

    public static LogListScanStages getListScanStagesByDepartment(Realm realm, int orderId, int departmentId, int userId) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).findFirst();

        return logListScanStages;
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
