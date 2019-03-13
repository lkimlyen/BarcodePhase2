package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.TimesConfirm;
import com.demo.architect.data.model.offline.DeliveryNoteModel;
import com.demo.architect.data.model.offline.DeliveryNoteWindowModel;
import com.demo.architect.data.model.offline.DetailError;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ListDepartmentQualityControl;
import com.demo.architect.data.model.offline.ListOrderQualityControl;
import com.demo.architect.data.model.offline.LogListFloorPagkaging;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogListScanStagesMain;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanConfirmModel;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.NumberInputConfirmModel;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;

import java.io.File;
import java.util.Collection;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;

public class DatabaseRealm {
    private Context context;
    private long userId = -1;

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
                        .schemaVersion(3)
                        .migration(new MyMigration())
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationMain);
                RealmHelper.getInstance().initRealm(true);
            }
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_TEST)) {
                RealmConfiguration realmConfigurationTest = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_TEST)
                        .schemaVersion(3)
                        .migration(new MyMigration())
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

    public <T extends RealmObject> T findFirstById(Class<T> clazz, long id) {
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

    public int countLogScanStagesWatingUpload(long orderId, int departmentId, int times) {
        Realm realm = getRealmInstance();
        final int count = LogListScanStages.countDetailWaitingUpload(realm, orderId, departmentId, userId, times);
        return count;
    }

    public int countAllDetailWaitingUpload(long orderId) {
        Realm realm = getRealmInstance();
        final int count = LogListScanStages.countAllDetailWaitingUpload(realm, orderId, userId);
        return count;
    }

    public List<LogScanStages> getListLogScanStagesUpload() {
        Realm realm = getRealmInstance();
        final List<LogScanStages> list = LogScanStages.getListScanStagesWaitingUpload(realm);
        return list;
    }


    public void addLogScanStagesAsync(final LogScanStages model, final long productId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.addLogScanStages(realm, model, productId);
            }
        });
    }

    public void addGroupScan(final List<GroupEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupScan.create(realm, list);
            }
        });
    }


    public void updateNumberScanStages(final long stagesId, final int numberInput) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.updateNumberInput(realm, stagesId, numberInput);
            }
        });
    }

    public void deleteScanStages(final long stagesId) {
        Realm realm = getRealmInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.deleteScanStages(realm, stagesId);
            }
        });
    }

    public LogListScanStages getListScanStages(long orderId, int departmentId, long userId, int times) {
        Realm realm = getRealmInstance();
        LogListScanStages logListScanStages = LogListScanStages.getListScanStagesByDepartment(realm, orderId, departmentId, userId, times);
        return logListScanStages;
    }

    public RealmResults<LogScanStages> getListScanStagesByModule(long orderId, int departmentId, int times, String module) {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStages> logListScanStages = LogListScanStages.getListScanStagesByModule(realm, orderId, departmentId, userId, times, module);
        return logListScanStages;
    }

    public RealmResults<GroupCode> getListGroupCode(long orderId) {
        Realm realm = getRealmInstance();
        RealmResults<GroupCode> results = GroupCode.getListGroupCodeByModule(realm, orderId, userId);
        return results;
    }

    public ProductDetail getProductDetail(final ProductEntity productEntity, int times) {
        Realm realm = getRealmInstance();
        ProductDetail productDetail = ProductDetail.getProductDetail(realm, productEntity, times, userId);
        if (productDetail == null) {
            productDetail = ProductDetail.create(realm, productEntity, times, userId);
        }
        return productDetail;

    }

    public void addOrderConfirm(final List<OrderConfirmEntity> list, final int times) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DeliveryNoteModel> deliveryNoteModels = realm.where(DeliveryNoteModel.class)
                        .equalTo("status", Constants.WAITING_UPLOAD).findAll();
                deliveryNoteModels.deleteAllFromRealm();
                RealmResults<LogScanConfirmModel> results = realm.where(LogScanConfirmModel.class)
                        .equalTo("status", Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();
                for (OrderConfirmEntity orderConfirmEntity : list) {
                    LogScanConfirmModel.createOrUpdate(realm, orderConfirmEntity, times, userId);
                }
            }
        });
    }

    public RealmResults<LogScanConfirmModel> getListConfirm() {
        Realm realm = getRealmInstance();
        final RealmResults<LogScanConfirmModel> results = LogScanConfirmModel.getListScanConfirm(realm);
        return results;
    }


    public List<LogScanConfirmModel> getListLogScanConfirm() {
        Realm realm = getRealmInstance();
        List<LogScanConfirmModel> results = LogScanConfirmModel.getListLogScanConfirm(realm);
        return results;
    }


    public void updateNumberLogConfirm(final long outputId, final int numberScan, final boolean scan) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmModel.updateNumberScan(realm, outputId, numberScan, scan);
            }
        });
    }

    public void updateStatusConfirm() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmModel.updateStatusScanConfirm(realm);
            }
        });
    }

    public void updateStatusScanStagesByOrder(final long orderId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogListScanStages.updateStatusScanStagesByOrder(realm, orderId, userId);
            }
        });
    }

    public void updateStatusScanStages(final long orderId, final int departmentId, final int times) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogListScanStages.updateStatusScanStages(realm, orderId, departmentId, times, userId);
            }
        });
    }

    public LogScanConfirmModel findConfirmByBarcode(String barcode) {
        Realm realm = getRealmInstance();
        LogScanConfirmModel logScanConfirmModel = LogScanConfirmModel.findConfirmByBarcode(realm, barcode);
        return logScanConfirmModel;
    }

    public void addImageModel(final long id, final String pathFile, final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type == 4) {
                    QualityControlWindowModel.updateListImage(realm, id, pathFile);
                } else {
                    QualityControlModel.updateListImage(realm, id, pathFile);
                }
            }
        });
    }

    public void deleteImageModel(final long id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageModel.delete(realm, id);
            }
        });
    }

    public void updateStatusAndServerIdImage(final long id, final long imageId, final long serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.updateStatusAndServerId(realm, id, imageId, serverId);
            }
        });
    }

    public RealmResults<LogListSerialPackPagkaging> getListScanPackaging(SOEntity soEntity, ApartmentEntity apartment) {
        Realm realm = getRealmInstance();
        RealmResults<LogListSerialPackPagkaging> listScanPackaging = LogScanPackaging.getListScanPackaging(realm,
                soEntity, apartment);
        return listScanPackaging;
    }


    public void deleteScanPackaging(final long logId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.deleteLogScanPackaging(realm, logId);
            }
        });

    }

    public void updateNumberScanPackaging(final long logId, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.updateNumberScanPackaging(realm, logId, number);
            }
        });

    }

    public ProductPackagingModel findProductPackaging(final long productId, final String serialPack) {
        Realm realm = getRealmInstance();
        ProductPackagingModel productPackagingModel = ProductPackagingModel.findProductPackaging(realm, productId, serialPack);
        return productPackagingModel;
    }

    public LogListOrderPackaging findOrderPackaging(final long orderId) {
        Realm realm = getRealmInstance();
        LogListOrderPackaging listOrderPackaging = LogListOrderPackaging.findOrderPackaging(realm, orderId);
        return listOrderPackaging;
    }

    public int getTotalScanBySerialPack(long orderId, long apartmentId, long moduleId, String serialPack) {
        Realm realm = getRealmInstance();
        int total = LogScanPackaging.getTotalScan(realm, orderId, apartmentId, moduleId, serialPack);
        return total;
    }

    public void addGroupCode(final String groupCode, final long orderId, final GroupCode[] listSelect) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.addGroupCode(realm, groupCode, orderId, listSelect, userId);
            }
        });
    }

    public void updateNumberGroup(final long groupId, final int numberGroup) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.updateNumberGroup(realm, groupId, numberGroup, userId);
            }
        });

    }

    public void detachedCodeStages(final List<ProductGroupEntity> list, final long orderId, final String groupCode) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.detachedCode(realm, list, orderId, userId);
            }
        });
    }

    public void removeItemInGroup(final ProductGroupEntity logScanStages, final long orderId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.removeItemInGroup(realm, logScanStages, orderId, userId);
            }
        });
    }

    public void addBarcodeScanPackaging(final ListModuleEntity module, final PackageEntity packageEntity, final ProductPackagingEntity productPackagingEntity, final long orderId, final long apartmentId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.createOrUpdateLogScanPackaging(realm, module, packageEntity, productPackagingEntity, orderId, apartmentId, userId);
            }
        });

    }

    public List<LogScanPackaging> getListScanPackaging(long orderId, long apartmentId, long moduleId, String serialPackId) {
        Realm realm = getRealmInstance();
        List<LogScanPackaging> result = LogScanPackaging.getListScanPackaging(realm,
                orderId, apartmentId, moduleId, serialPackId);
        return result;
    }

    public void updateStatusScanPackaging(final long orderId, final long apartmentId, final long moduleId, final String serialPack, final long serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.updateStatusScanPackaging(realm, orderId, apartmentId, moduleId, serialPack, serverId);

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

    public RealmResults<QualityControlModel> getListQualityControl() {
        Realm realm = getRealmInstance();
        RealmResults<QualityControlModel> results = QualityControlModel.getListQualityControl(realm);
        return results;
    }

    public QualityControlModel getDetailQualityControl(long id) {
        Realm realm = getRealmInstance();
        QualityControlModel results = QualityControlModel.getDetailQualityControl(realm, id);
        return results;
    }

    public RealmList<Integer> getListReasonQualityControl(long id, int type) {
        Realm realm = getRealmInstance();
        RealmList<Integer> results;
        if (type != 4) {

            results = QualityControlModel.getListReasonQualityControl(realm, id);
        } else {
            results = QualityControlWindowModel.getListReasonQualityControl(realm, id);
        }
        return results;
    }

    public void saveBarcodeQC(final long orderId, final int departmentId, final String machineName, final String violator, final String qcCode, final ProductEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.create(realm, orderId, departmentId, machineName, violator, qcCode, productEntity, userId);
            }
        });
    }

    public void updateDetailErrorQC(final long id, final int numberFailed, final String description, final Collection<Integer> idList) {
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

    public void updateImageIdAndStatus(final long qcId, final long id, final long imageId, final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type != 4) {

                    QualityControlModel.updateImageIdAndStatus(realm, qcId, id, imageId);
                } else {

                    QualityControlWindowModel.updateImageIdAndStatus(realm, qcId, id, imageId);
                }
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

    public boolean checkNumberProductInGroupCode(ProductEntity model) {
        Realm realm = getRealmInstance();
        boolean number = GroupCode.checkNumberProductInGroupCode(realm, model, userId);
        return number;
    }

    public void updateGroupCode(final String groupCode, final long orderId, final GroupCode[] listSelect) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.updateGroupCode(realm, groupCode, orderId, listSelect, userId);
            }
        });
    }


    public void confirmAllProductReceive() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmModel.confirmAllReceive(realm);
            }
        });
    }

    public void cancelConfirmAllProductReceive() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmModel.cancelConfirmAllReceive(realm);
            }
        });
    }

    public boolean getCheckedConfirmAll(final long orderId, final int departmentId, final int times) {
        Realm realm = getRealmInstance();
        boolean b = TimesConfirm.getCheckedConfirmAll(realm, orderId, departmentId, times, userId);
        return b;
    }

    public void deleteScanGroupCode(final long id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GroupCode.deleteScanGroupCode(realm, id, userId);
            }
        });
    }

    public RealmResults<LogScanStages> getScanByProductDetailId(LogScanStages logScanStages) {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStages> list = LogScanStages.getScanByProductDetailId(realm, logScanStages);
        return list;
    }

    public int totalNumberScanGroup(long productDetailId) {
        Realm realm = getRealmInstance();
        int aint = GroupCode.totalNumberScanGroup(realm, productDetailId);
        return aint;
    }

    public void deleteQC(final long id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.deleteQC(realm, id);
            }
        });
    }


    public void updateNumberTotalProduct(final List<ProductEntity> entity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ProductDetail.updateProductDetail(realm, entity, userId);
            }
        });
    }

    public void deleteDataLocal() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DetailError> detailErrors = realm.where(DetailError.class).findAll();

                detailErrors.deleteAllFromRealm();
                RealmResults<GroupCode> groupCodes = realm.where(GroupCode.class).findAll();
                groupCodes.deleteAllFromRealm();

                RealmResults<GroupScan> groupScans = realm.where(GroupScan.class).findAll();
                groupScans.deleteAllFromRealm();
                RealmResults<ImageModel> imageModels = realm.where(ImageModel.class).findAll();

                for (ImageModel imageModel : imageModels) {
                    File file = new File(imageModel.getPathFile());
                    if (file.exists()) {
                        file.delete();
                    }
                }

                imageModels.deleteAllFromRealm();
                RealmResults<ListDepartmentQualityControl> listDepartmentQualityControls = realm.where(ListDepartmentQualityControl.class).findAll();
                listDepartmentQualityControls.deleteAllFromRealm();
                RealmResults<ListOrderQualityControl> listOrderQualityControls = realm.where(ListOrderQualityControl.class).findAll();
                listOrderQualityControls.deleteAllFromRealm();

                RealmResults<LogListModulePagkaging> logListModulePagkagings = realm.where(LogListModulePagkaging.class).findAll();
                logListModulePagkagings.deleteAllFromRealm();
                RealmResults<LogListFloorPagkaging> logListFloorPagkagings = realm.where(LogListFloorPagkaging.class).findAll();
                logListFloorPagkagings.deleteAllFromRealm();
                RealmResults<LogListOrderPackaging> logListOrderPackagings = realm.where(LogListOrderPackaging.class).findAll();
                logListOrderPackagings.deleteAllFromRealm();
                RealmResults<LogListScanStages> logListScanStages = realm.where(LogListScanStages.class).findAll();
                logListScanStages.deleteAllFromRealm();
                RealmResults<LogListScanStagesMain> logListScanStagesMains = realm.where(LogListScanStagesMain.class).findAll();
                logListScanStagesMains.deleteAllFromRealm();
                RealmResults<LogListSerialPackPagkaging> logListSerialPackPagkagings = realm.where(LogListSerialPackPagkaging.class).findAll();
                logListSerialPackPagkagings.deleteAllFromRealm();

                RealmResults<LogScanConfirmModel> logScanConfirmModels = realm.where(LogScanConfirmModel.class).findAll();
                logScanConfirmModels.deleteAllFromRealm();
                RealmResults<LogScanPackaging> logScanPackagings = realm.where(LogScanPackaging.class).findAll();
                logScanPackagings.deleteAllFromRealm();
                RealmResults<LogScanStages> logScanStages = realm.where(LogScanStages.class).findAll();
                logScanStages.deleteAllFromRealm();
                RealmResults<NumberInputConfirmModel> numberInputConfirmModels = realm.where(NumberInputConfirmModel.class).findAll();
                numberInputConfirmModels.deleteAllFromRealm();
                RealmResults<NumberInputModel> numberInputModels = realm.where(NumberInputModel.class).findAll();
                numberInputModels.deleteAllFromRealm();
                RealmResults<ProductDetail> productDetails = realm.where(ProductDetail.class).findAll();
                productDetails.deleteAllFromRealm();
                RealmResults<ProductPackagingModel> productPackagingModels = realm.where(ProductPackagingModel.class).findAll();
                productPackagingModels.deleteAllFromRealm();
                RealmResults<QualityControlModel> qualityControlModels = realm.where(QualityControlModel.class).findAll();
                qualityControlModels.deleteAllFromRealm();

            }
        });
    }

    public List<GroupScan> getListGroupScanVersion() {
        Realm realm = getRealmInstance();
        List<GroupScan> list = LogListScanStages.getListGroupScanVersion(realm);
        return list;
    }

    public void saveListProductDetail(final List<ProductEntity> entity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // ProductDetail.create(realm, entity, userId);
            }
        });
    }

    public void deleteAllProductDetail() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProductDetail> results = realm.where(ProductDetail.class).equalTo("status",Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();
            }
        });
    }

    public RealmResults<LogScanStages> getAllListStages() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStages> results = realm.where(LogScanStages.class).equalTo("status",Constants.WAITING_UPLOAD).findAll();
        return results;
    }

    public void deleteAllScanStages() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProductDetail> results = realm.where(ProductDetail.class).equalTo("status",Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();

                RealmResults<LogScanStages> results1 = realm.where(LogScanStages.class).equalTo("status",Constants.WAITING_UPLOAD).findAll();
                results1.deleteAllFromRealm();

                RealmResults<GroupScan> results2 = realm.where(GroupScan.class).findAll();
                results2.deleteAllFromRealm();
            }
        });
    }

    public ProductDetailWindowModel getProductDetailWindow(ProductWindowEntity model) {
        Realm realm = getRealmInstance();
        ProductDetailWindowModel productDetail = ProductDetailWindowModel.getProductDetail(realm, model, userId);
        if (productDetail == null) {
            productDetail = ProductDetailWindowModel.create(realm, model, userId);
        }
        return productDetail;
    }

    public void addLogScanStagesWindow(final LogScanStagesWindowModel logScanStages) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStagesWindowModel.addLogScanStages(realm, logScanStages);
            }
        });
    }

    public void updateNumberScanStagesWindow(final long stagesId, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStagesWindowModel.updateNumberInput(realm, stagesId, number);
            }
        });
    }

    public void deleteAlScanStagesWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProductDetailWindowModel> results = realm.where(ProductDetailWindowModel.class)
                        .equalTo("status",Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();

                RealmResults<LogScanStagesWindowModel> results1 = realm.where(LogScanStagesWindowModel.class)
                        .equalTo("status",Constants.WAITING_UPLOAD)
                        .findAll();
                results1.deleteAllFromRealm();
            }
        });
    }

    public RealmResults<LogScanStagesWindowModel> getAllListStagesWindow() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStagesWindowModel> results = LogScanStagesWindowModel.getAllList(realm);
        return results;
    }

    public void deleteScanStagesWindow(final long stagesId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStagesWindowModel.deleteScanStages(realm, stagesId);
            }
        });
    }

    public List<LogScanStagesWindowModel> getListLogScanStagesWindowUpload() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStagesWindowModel> results = LogScanStagesWindowModel.getAllList(realm);
        return realm.copyFromRealm(results);
    }

    public void addOrderConfirmWindow(final List<OrderConfirmWindowEntity> list) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DeliveryNoteWindowModel> deliveryNoteModels = realm.where(DeliveryNoteWindowModel.class)
                        .equalTo("status", Constants.WAITING_UPLOAD).findAll();
                deliveryNoteModels.deleteAllFromRealm();
                RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();
                for (OrderConfirmWindowEntity orderConfirmEntity : list) {
                    LogScanConfirmWindowModel.createOrUpdate(realm, orderConfirmEntity, userId);
                }
            }
        });
    }

    public RealmResults<LogScanConfirmWindowModel> getListConfirmWindow() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class)
                .equalTo("state", false)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;
    }

    public LogScanConfirmWindowModel findConfirmByBarcodeInWindow(String barcode) {
        Realm realm = getRealmInstance();
        LogScanConfirmWindowModel model = LogScanConfirmWindowModel.findConfirmByBarcode(realm, barcode);
        return model;
    }

    public void updateNumnberLogConfirmWindow(final long outputId, final int number, final boolean scan) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmWindowModel.updateNumberScan(realm, outputId, number, scan);
            }
        });
    }

    public List<LogScanConfirmWindowModel> getListLogScanConfirmWindow() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanConfirmWindowModel> results = realm.where(LogScanConfirmWindowModel.class)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public void cancelConfirmAllProductReceiveWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmWindowModel.cancelConfirmAllReceive(realm);
            }
        });
    }

    public void confirmAllProductReceiveWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmWindowModel.confirmAllReceive(realm);
            }
        });
    }

    public void updateStatusLogConfirmWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanConfirmWindowModel.updateStatusScanConfirm(realm);
            }
        });
    }

    public Boolean checkBarcodeExistInQC(String barcode) {
        Realm realm = getRealmInstance();
        boolean exist = QualityControlModel.checkBarcodeExistInQC(realm, barcode);
        return exist;
    }

    public void deleteAlLQC() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlModel.deleteAlLQC(realm);
            }
        });
    }

    public void saveBarcodeQCWindow(final String machineName, final String violator, final String qcCode, final ProductWindowEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlWindowModel.create(realm, machineName, violator, qcCode, productEntity, userId);
            }
        });
    }

    public RealmResults<QualityControlWindowModel> getListQualityControlWindow() {
        Realm realm = getRealmInstance();
        RealmResults<QualityControlWindowModel> results = QualityControlWindowModel.getListQualityControl(realm);
        return results;
    }

    public void deleteAlLQCWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlWindowModel.deleteAlLQC(realm);
            }
        });
    }

    public Boolean checkBarcodeExistInQCWindow(String barcode) {
        Realm realm = getRealmInstance();
        boolean exist = QualityControlWindowModel.checkBarcodeExistInQC(realm, barcode);
        return exist;
    }

    public QualityControlWindowModel getDetailQualityControlWindow(long id) {
        Realm realm = getRealmInstance();
        QualityControlWindowModel model = QualityControlWindowModel.getDetailQualityControl(realm, id);
        return model;
    }

    public void updateDetailErrorQCWindow(final long id, final int numberFailed, final String description, final Collection<Integer> idList) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlWindowModel.updateDetailErrorQC(realm, id, numberFailed, description, idList);
            }
        });
    }

    public List<QualityControlWindowModel> getListQualityControlUploadWindow() {
        Realm realm = getRealmInstance();
        List<QualityControlWindowModel> list = QualityControlWindowModel.getListQualityControlUpload(realm);
        return list;
    }

    public void updateStatusQCWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlWindowModel.updateStatusQC(realm);
            }
        });
    }

    public void updateStatusLogStagesWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStagesWindowModel.updateStatusLogStagesWindow(realm);
            }
        });
    }

    public void updateStatusLogStages() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanStages.updateStatusLogStages(realm);
            }
        });
    }

    public class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();


            // Migrate to version 2: Add a primary key + object references
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     @PrimaryKey
            //     private int age;
            //     private Dog favoriteDog;
            //     private RealmList<Dog> dogs;
            //     // getters and setters left out for brevity
            // }
            if (oldVersion == 1) {
                schema.get("LogScanConfirmModel")
                        .addField("productId", long.class)
                        .addField("isPrint", boolean.class);
                oldVersion++;
            }
            if (oldVersion == 2) {
                schema.create("DeliveryNoteModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("deliveryNoteId", long.class)
                        .addField("orderId", long.class)
                        .addField("outputId", long.class)
                        .addField("productDetailId", long.class)
                        .addField("numberOut", int.class)
                        .addField("numberRest", int.class)
                        .addField("numberUsed", int.class)
                        .addField("numberConfirm", int.class);
                schema.get("LogScanConfirmModel")
                        .removeField("lastIDOutput")
                        .addField("deliveryNoteId", long.class)
                        .removeField("numberScanOut")
                        .addField("numberRestInTimes", int.class)
                        .removeField("list").addField("numberUsedInTimes", int.class)
                        .addRealmObjectField("deliveryNoteModel", schema.get("DeliveryNoteModel"));


                oldVersion++;
            }


        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof MyMigration; // obj instance of your Migration class name, here My class is Migration.
        }

        @Override
        public int hashCode() {
            return MyMigration.class.hashCode();
        }
    }

}
