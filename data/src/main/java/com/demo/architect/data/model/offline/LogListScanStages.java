package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogListScanStages extends RealmObject {
    @PrimaryKey
    private int id;
    private int departmentId;
    private int status;
    private int times;
    private int userId;
    private String date;

    public RealmList<LogScanStages> getList() {
        return list;
    }

    @SuppressWarnings("unused")
    private RealmList<LogScanStages> list;

    public RealmList<ListGroupCode> getListGroupCodes() {
        return listGroupCodes;
    }

    public void setListGroupCodes(RealmList<ListGroupCode> listGroupCodes) {
        this.listGroupCodes = listGroupCodes;
    }

    @SuppressWarnings("unused")
    private RealmList<ListGroupCode> listGroupCodes;


    public LogListScanStages() {
    }

    public LogListScanStages(int id, int departmentId, int status, int userId, int times, String date) {
        this.id = id;
        this.departmentId = departmentId;
        this.status = status;
        this.userId = userId;
        this.times = times;
        this.date = date;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogListScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static int countDetailWaitingUpload(Realm realm, int orderId, int departmentId, int userId, int times) {
        int count = 0;
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (logListScanStagesMain != null) {
            LogListScanStages logListScanStages = logListScanStagesMain.getList().where().equalTo("departmentId", departmentId)
                    .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).equalTo("times", times)
                    .equalTo("date", DateUtils.getShortDateCurrent()).findFirst();
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

    public static List<LogScanStages> getListScanStagesWaitingUpload(Realm realm, int userId) {
        List<LogScanStages> list = new ArrayList<>();
        RealmList<LogScanStages> realmList = new RealmList<>();
        RealmResults<LogListScanStages> logListScanStages = realm.where(LogListScanStages.class)
                .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).findAll();
        for (LogListScanStages log : logListScanStages) {
            realmList.addAll(log.getList());
        }
        if (logListScanStages != null) {
            list = realm.copyFromRealm(realmList);
        }
        return list;
    }

    public static LogListScanStages getListScanStagesByDepartment(Realm realm, int orderId, int departmentId, int userId, int times) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = null;
        if (logListScanStagesMain != null) {
            if (logListScanStagesMain.getList() != null) {
                logListScanStages = logListScanStagesMain.getList().where()
                        .equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("departmentId", departmentId)
                        .equalTo("times", times)
                        .equalTo("userId", userId).findFirst();
                if (logListScanStages == null) {
                    realm.beginTransaction();
                    RealmList<LogListScanStages> parentList = logListScanStagesMain.getList();
                    logListScanStages = new LogListScanStages(LogListScanStages.id(realm) + 1, departmentId,
                            Constants.WAITING_UPLOAD, userId, times, DateUtils.getShortDateCurrent());
                    logListScanStages = realm.copyToRealm(logListScanStages);
                    parentList.add(logListScanStages);
                    realm.commitTransaction();
                }
            }
        } else {
            realm.beginTransaction();
            logListScanStagesMain = new LogListScanStagesMain(orderId);
            logListScanStagesMain = realm.copyToRealm(logListScanStagesMain);
            RealmList<LogListScanStages> parentList = logListScanStagesMain.getList();
            logListScanStages = new LogListScanStages(LogListScanStages.id(realm) + 1, departmentId,
                    Constants.WAITING_UPLOAD, userId, times, DateUtils.getShortDateCurrent());
            logListScanStages = realm.copyToRealm(logListScanStages);
            parentList.add(logListScanStages);
            realm.commitTransaction();
        }

        return logListScanStages;
    }

    public static RealmResults<LogScanStages> getListScanStagesByModule(Realm realm, int orderId, int departmentId, int userId, int times,String module) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("departmentId", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId).findFirst();
        RealmResults<LogScanStages> results = logListScanStages.getList().where().equalTo("module",module).findAll();

        return results;
    }

    public static void updateStatusScanStagesByOrder(Realm realm, int orderId, int userId) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        RealmResults<LogListScanStages> logListScanStages = null;
        if (logListScanStagesMain != null) {
            if (logListScanStagesMain.getList() != null) {
                logListScanStages = logListScanStagesMain.getList().where()
                        .equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("userId", userId).findAll();

                for (LogListScanStages log : logListScanStages) {
                    log.setStatus(Constants.COMPLETE);
                }

            }
        }

    }

    public static void updateStatusScanStages(Realm realm, int userId) {
        RealmResults<LogListScanStages> logListScanStages = realm.where(LogListScanStages.class).equalTo("userId", userId).findAll();
        if (logListScanStages != null) {
            for (LogListScanStages log : logListScanStages) {
                log.setStatus(Constants.COMPLETE);
            }

        }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setList(RealmList<LogScanStages> list) {
        this.list = list;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public static RealmResults<ListGroupCode> getListGroupCodeByModule(Realm realm, int orderId, int departmentId, int userId, int times, String module) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("departmentId", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId).findFirst();
        RealmResults<ListGroupCode> results = logListScanStages.getListGroupCodes().where().equalTo("module",module).findAll();

        return results;
    }
}
