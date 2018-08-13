package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.ConfirmInputModel;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DatabaseRealm {
    private Context context;
    private int userId = -1;

    public DatabaseRealm() {
    }

    public DatabaseRealm(Context context) {
        this.context = context;
    }

    public Realm getRealmInstance() {
        if (!RealmHelper.getInstance().getInitRealm()) {
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_MAIN)) {
                RealmConfiguration realmConfigurationMain = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_MAIN)
                        .schemaVersion(1)
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationMain);
                RealmHelper.getInstance().initRealm(true);
            }
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_TEST)) {
                RealmConfiguration realmConfigurationTest = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_TEST)
                        .schemaVersion(1)
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationTest);
                RealmHelper.getInstance().initRealm(true);

            }

            userId = SharedPreferenceHelper.getInstance(context).getUserObject().getId();

        }
        return Realm.getDefaultInstance();
    }

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> void addItemAsync(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    public <T extends RealmObject> void insertOrUpdate(final T item) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }

    public <T extends RealmObject> T findFirst(Class<T> clazz) {
        return getRealmInstance().where(clazz).findFirst();
    }

    public <T extends RealmObject> T findFirstById(Class<T> clazz, int id) {
        return getRealmInstance().where(clazz).equalTo("id", id).findFirst();
    }

    public void close() {
        getRealmInstance().close();
    }

    public <T extends RealmObject> void delete(Class<T> clazz) {
        Realm realm = getRealmInstance();
        // obtain the results of a query
        final RealmResults<T> results = realm.where(clazz).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

            }
        });
    }

    public int countLogScanStagesWatingUpload(int orderId, int departmentId) {

        Realm realm = getRealmInstance();
        final int count = LogListScanStages.countDetailWaitingUpload(realm, orderId, departmentId, userId);
        return count;
    }

    public List<LogScanStages> getListLogScanStagesUpload(int orderId) {
        Realm realm = getRealmInstance();
        final List<LogScanStages> list = LogListScanStages.getListScanStagesWaitingUpload(realm, orderId, userId);
        return list;
    }

    public void addLogScanStagesAsync(final LogScanStages model) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.addLogScanStages(realm, model);
            }
        });
    }

    public void updateNumberScanStages(final int stagesId, final int numberInput) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.updateNumberInput(realm, stagesId, numberInput);
            }
        });
    }

    public void deleteScanStages(final int stagesId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.deleteScanStages(realm, stagesId);
            }
        });
    }

    public LogListScanStages getListScanStages(int orderId, int departmentId, int userId) {
        Realm realm = getRealmInstance();
        LogListScanStages logListScanStages = LogListScanStages.getListScanStagesByDepartment(realm, orderId, departmentId, userId);
        return logListScanStages;
    }

    public ProductDetail getProductDetail(final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        final ProductDetail productDetail = ProductDetail.getProductDetail(realm, productEntity);
        return productDetail;

    }

    public void addOrderConfirm(final List<OrderConfirmEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (OrderConfirmEntity orderConfirmEntity : list) {
                    ConfirmInputModel.create(realm, orderConfirmEntity);
                }
            }
        });
    }

    public RealmResults<ConfirmInputModel> getListConfirm(int times) {
        Realm realm = getRealmInstance();
        RealmResults<ConfirmInputModel> results = ConfirmInputModel.getListScanConfirm(realm, times);
        return results;
    }
}
