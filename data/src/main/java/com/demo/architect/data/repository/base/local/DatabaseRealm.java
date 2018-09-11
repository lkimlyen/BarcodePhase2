package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.model.offline.QualityControlModel;

import java.util.ArrayList;
import java.util.Collection;
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

    public int countLogScanStagesWatingUpload(int orderId, int departmentId, int times) {

        Realm realm = getRealmInstance();
        final int count = LogListScanStages.countDetailWaitingUpload(realm, orderId, departmentId, userId, times);
        return count;
    }

    public List<LogScanStages> getListLogScanStagesUpload(int orderId) {
        Realm realm = getRealmInstance();
        final List<LogScanStages> list = LogListScanStages.getListScanStagesWaitingUpload(realm, orderId, userId);
        return list;
    }

    public List<LogScanStages> getListLogScanStagesUpload() {
        Realm realm = getRealmInstance();
        final List<LogScanStages> list = LogListScanStages.getListScanStagesWaitingUpload(realm, userId);
        return list;
    }


    public void addLogScanStagesAsync(final LogScanStages model, final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.addLogScanStages(realm, model, productEntity);
            }
        });
    }

    public void updateNumberScanStages(final int stagesId, final int numberInput) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
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

    public LogListScanStages getListScanStages(int orderId, int departmentId, int userId, int times) {
        Realm realm = getRealmInstance();
        LogListScanStages logListScanStages = LogListScanStages.getListScanStagesByDepartment(realm, orderId, departmentId, userId, times);
        return logListScanStages;
    }

    public RealmResults<LogScanStages> getListScanStagesByModule(int orderId, int departmentId, int times, String module) {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStages> logListScanStages = LogListScanStages.getListScanStagesByModule(realm, orderId, departmentId, userId, times, module);
        return logListScanStages;
    }

    public RealmResults<GroupCode> getListGroupCodeByModule(int orderId, String module) {
        Realm realm = getRealmInstance();
        RealmResults<GroupCode> results = GroupCode.getListGroupCodeByModule(realm, orderId, userId, module);
        return results;
    }

    public ProductDetail getProductDetail(final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        final ProductDetail productDetail = ProductDetail.getProductDetail(realm, productEntity, userId);
        return productDetail;

    }

    public void addOrderConfirm(final List<OrderConfirmEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (OrderConfirmEntity orderConfirmEntity : list) {
                    LogScanConfirm.createOrUpdate(realm, orderConfirmEntity, userId);
                }
            }
        });
    }

    public RealmResults<LogScanConfirm> getListConfirm(int orderId, final int departmentIdOut, int times) {
        Realm realm = getRealmInstance();
        final RealmResults<LogScanConfirm> results = LogScanConfirm.getListScanConfirm(realm, orderId, departmentIdOut, times, userId);
        return results;
    }

    public int countListConfirmByTimesWaitingUpload(int orderId, final int departmentIdOut, int times) {
        Realm realm = getRealmInstance();
        final int count = LogScanConfirm.countListConfirmByTimesWaitingUpload(realm, orderId, departmentIdOut, times, userId);
        return count;
    }

    public List<LogScanConfirm> getListLogScanConfirm() {
        Realm realm = getRealmInstance();
        List<LogScanConfirm> results = LogScanConfirm.getListLogScanConfirm(realm, userId);
        return results;
    }


    public void updateNumberLogConfirm(final int orderId, final int orderProductId, final int departmentIdOut, final int times, final int numberScan, final boolean scan) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirm.updateNumberScan(realm, orderId, orderProductId, departmentIdOut, times, numberScan, userId, scan);
            }
        });
    }

    public void updateStatusConfirm() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirm.updateStatusScanConfirm(realm, userId);
            }
        });
    }

    public void updateStatusScanStagesByOrder(final int orderId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogListScanStages.updateStatusScanStagesByOrder(realm, orderId, userId);
            }
        });
    }

    public void updateStatusScanStages() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogListScanStages.updateStatusScanStages(realm, userId);
            }
        });
    }

    public LogScanConfirm findConfirmByBarcode(String barcode, int orderId,
                                               int departmentIDOut, int times) {
        Realm realm = getRealmInstance();
        LogScanConfirm logScanConfirm = LogScanConfirm.findConfirmByBarcode(realm, barcode, orderId, departmentIDOut, times, userId);
        return logScanConfirm;
    }

    public void addImageModel(final int id, final String pathFile) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateListImage(realm, id, pathFile);
            }
        });
    }

    public void deleteImageModel(final int id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageModel.delete(realm, id);
            }
        });
    }

    public void updateStatusAndServerIdImage(final int id, final int imageId, final int serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateStatusAndServerId(realm, id, imageId, serverId);
            }
        });
    }

    public RealmResults<LogScanPackaging> getListScanPackaging(SOEntity soEntity, ModuleEntity moduleEntity, ApartmentEntity apartment, CodePackEntity codePack) {
        Realm realm = getRealmInstance();
        RealmResults<LogScanPackaging> listScanPackaging = LogScanPackaging.getListScanPackaging(realm,
                soEntity, moduleEntity, apartment, codePack);
        return listScanPackaging;
    }


    public void deleteScanPackaging(final int logId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.deleteLogScanPackaging(realm, logId);
            }
        });

    }

    public void updateNumberScanPackaging(final int logId, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.updateNumberScanPackaging(realm, logId, number);
            }
        });

    }

    public ProductPackagingModel findProductPackaging(final int productId, final String serialPack) {
        Realm realm = getRealmInstance();
        ProductPackagingModel productPackagingModel = ProductPackagingModel.findProductPackaging(realm, productId, serialPack);
        return productPackagingModel;
    }

    public LogListOrderPackaging findOrderPackaging(final int orderId) {
        Realm realm = getRealmInstance();
        LogListOrderPackaging listOrderPackaging = LogListOrderPackaging.findOrderPackaging(realm, orderId);
        return listOrderPackaging;
    }

    public int getTotalScanBySerialPack(int orderId, int apartmentId, int moduleId, String serialPack) {
        Realm realm = getRealmInstance();
        int total = LogScanPackaging.getTotalScan(realm, orderId, apartmentId, moduleId, serialPack);
        return total;
    }

    public void addGroupCode(final String groupCode, final int orderId, final String module, final GroupCode[] listSelect) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.addGroupCode(realm, groupCode, orderId, module, listSelect, userId);
            }
        });
    }

    public boolean updateNumberGroup(final ProductEntity productEntity, final int groupId, final int numberGroup) {
        Realm realm = getRealmInstance();
        boolean b = GroupCode.updateNumberGroup(realm, productEntity, groupId, numberGroup, userId);
        return b;
    }

    public void detachedCodeStages(final int orderId, final String module, final String groupCode) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.detachedCode(realm, orderId, module, groupCode, userId);
            }
        });
    }

    public void removeItemInGroup(final String groupCode, final GroupCode logScanStages, final int orderId, final String module) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.removeItemInGroup(realm, groupCode, logScanStages, orderId, module, userId);
            }
        });
    }

    public void addBarcodeScanPackaging(final ProductPackagingEntity product, final String barcode, final int orderId, final int apartmentId, final int moduleId, final String packCode, final String serialPack) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.createOrUpdateLogScanPackaging(realm, product, barcode, orderId, apartmentId,
                        moduleId, packCode, serialPack);
            }
        });

    }

    public List<LogScanPackaging> getListScanPackaging(int orderId, int apartmentId, int moduleId, String serialPack) {
        Realm realm = getRealmInstance();
        List<LogScanPackaging> result = LogScanPackaging.getListScanPackaging(realm,
                orderId, apartmentId, moduleId, serialPack);
        return result;
    }

    public void updateStatusScanPackaging(final int serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.updateStatusScanPackaging(realm, serverId);
            }
        });

    }

    public void deleteAllItemLogScanPackaging() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.deleteAllItemLogScanPackaging(realm);
            }
        });
    }

    public RealmResults<QualityControlModel> getListQualityControl(int orderId, int departmentId) {
        Realm realm = getRealmInstance();
        RealmResults<QualityControlModel> results = QualityControlModel.getListQualityControl(realm, orderId, departmentId, userId);
        return results;
    }

    public QualityControlModel getDetailQualityControl(int id) {
        Realm realm = getRealmInstance();
        QualityControlModel results = QualityControlModel.getDetailQualityControl(realm, id);
        return results;
    }

    public RealmList<Integer> getListReasonQualityControl(int id) {
        Realm realm = getRealmInstance();
        RealmList<Integer> results = QualityControlModel.getListReasonQualityControl(realm, id);
        return results;
    }

    public void saveBarcodeQC(final int orderId, final int departmentId, final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.create(realm, orderId, departmentId, productEntity, userId);
            }
        });
    }

    public void updateDetailErrorQC(final int id, final int numberFailed, final String description, final Collection<Integer> idList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateDetailErrorQC(realm, id, numberFailed, description, idList);
            }
        });
    }

    public List<QualityControlModel> getListQualityControlUpload() {
        Realm realm = getRealmInstance();
        List<QualityControlModel> result = QualityControlModel.getListQualityControlUpload(realm, userId);
        return result;
    }

    public void updateImageIdAndStatus(final int qcId, final int id, final int imageId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateImageIdAndStatus(realm, qcId, id, imageId);
            }
        });
    }

    public void updateStatusQC() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateStatusQC(realm, userId);
            }
        });
    }

    public void addGroupCode(final String groupCode, final LogScanStages logScanStages, final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //  LogScanStages.addGroupCode(realm, groupCode, logScanStages, productEntity, userId);
            }
        });
    }

    public void addGroupCode(final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.create(realm, productEntity, userId);
            }
        });
    }

    public boolean checkProductExistInGroupCode(ProductEntity model) {
        Realm realm = getRealmInstance();
        boolean exist = GroupCode.checkProductExistInGroupCode(realm, model, userId);
        return exist;
    }

    public boolean checkNumberProductInGroupCode(ProductEntity model) {
        Realm realm = getRealmInstance();
        boolean number = GroupCode.checkNumberProductInGroupCode(realm, model, userId);
        return number;
    }

    public RealmResults<ListGroupCode> getListGroupCode(int orderId, String module) {
        Realm realm = getRealmInstance();
        RealmResults<ListGroupCode> results = GroupCode.getListGroupCode(realm, orderId, module, userId);
        return results;
    }

    public void updateGroupCode(final String groupCode, final int orderId, final String module, final GroupCode[] listSelect) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.updateGroupCode(realm, groupCode, orderId, module, listSelect, userId);
            }
        });
    }

    private List<GroupCode>groupCodeList;

    public List<GroupCode> updateNumberGroup(final int id, final int number) {
        groupCodeList = new ArrayList<>();
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                groupCodeList = GroupCode.updateNumberGroup(realm, id, number);

            }
        });

        return groupCodeList;
    }

    public void confirmAllProductReceive(final int orderId, final int departmentId, final int times) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirm.confirmAllReceive(realm, orderId, departmentId,times, userId);
            }
        });
    }
}
