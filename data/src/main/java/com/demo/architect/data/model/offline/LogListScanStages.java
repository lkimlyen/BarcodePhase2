package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class LogListScanStages extends RealmObject {
   @PrimaryKey
    private long id;
    private int departmentId;
    private int status;
    private int times;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setList(RealmList<LogScanStages> list) {
        this.list = list;
    }

    private long userId;
    private String date;

    public RealmList<LogScanStages> getList() {
        return list;
    }

    @SuppressWarnings("unused")
    private RealmList<LogScanStages> list;

    public LogListScanStages() {
    }

    public LogListScanStages(long id, int departmentId, int status, long userId, int times, String date) {
        this.id = id;
        this.departmentId = departmentId;
        this.status = status;
        this.userId = userId;
        this.times = times;
        this.date = date;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(LogListScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static int countDetailWaitingUpload(Realm realm, long orderId, int departmentId, long userId, int times) {
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

    public static int countAllDetailWaitingUpload(Realm realm, long orderId, long userId) {
        int count = 0;
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        if (logListScanStagesMain != null) {
            LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                    .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId)
                    .lessThan("date", new Date(DateUtils.getShortDateCurrent()))
                    .findFirst();
            if (logListScanStages != null) {
                count = logListScanStages.getList().size();
            }
        }
        return count;
    }

    public static List<LogScanStages> getListScanStagesWaitingUpload(Realm realm, long orderId, long userId) {
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

    public static HashMap<List<LogScanStages>, Set<GroupScan>> getListScanStagesWaitingUpload(Realm realm, long userId) {
        HashMap<List<LogScanStages>, Set<GroupScan>> hashMap = new HashMap<>();
        List<LogScanStages> list = new ArrayList<>();
        Set<GroupScan> groupScanSet = new HashSet<>();

        RealmList<LogScanStages> realmList = new RealmList<>();
        RealmResults<LogListScanStages> logListScanStages = realm.where(LogListScanStages.class)
                .equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).findAll();
        for (LogListScanStages log : logListScanStages) {
            realmList.addAll(log.getList());
            for (LogScanStages logScanStages : list) {
                GroupScan groupScan = realm.where(GroupScan.class).equalTo("masterGroupId", logScanStages.getMasterGroupId()).findFirst();
                groupScanSet.add(groupScan);
            }
        }
        if (logListScanStages != null) {
            list.addAll(realm.copyFromRealm(realmList));
        }
        hashMap.put(list,groupScanSet);
        return hashMap;
    }

    public static LogListScanStages getListScanStagesByDepartment(Realm realm, long orderId, int departmentId, long userId, int times) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = null;
        if (logListScanStagesMain != null) {
            if (logListScanStagesMain.getList() != null) {
                logListScanStages = logListScanStagesMain.getList().where()
                        .equalTo("status", Constants.WAITING_UPLOAD)
                        .equalTo("departmentId", departmentId)
                        .equalTo("times", times)
                        .equalTo("date", DateUtils.getShortDateCurrent())
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

    public static RealmResults<LogScanStages> getListScanStagesByModule(Realm realm, long orderId, int departmentId, long userId, int times, String module) {
        LogListScanStagesMain logListScanStagesMain = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        LogListScanStages logListScanStages = logListScanStagesMain.getList().where()
                .equalTo("status", Constants.WAITING_UPLOAD)
                .equalTo("departmentId", departmentId)
                .equalTo("times", times)
                .equalTo("userId", userId).findFirst();
        RealmResults<LogScanStages> results = logListScanStages.getList().where().equalTo("module", module).findAll();

        return results;
    }

    public static void updateStatusScanStagesByOrder(Realm realm, long orderId, long userId) {
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

    public static void updateStatusScanStages(Realm realm, long userId) {
        RealmResults<LogListScanStages> logListScanStages = realm.where(LogListScanStages.class).equalTo("status", Constants.WAITING_UPLOAD).equalTo("userId", userId).findAll();

        if (logListScanStages.size() > 0) {
            for (LogListScanStages log : logListScanStages) {
                log.setStatus(Constants.COMPLETE);
            }
        }

    }


}
