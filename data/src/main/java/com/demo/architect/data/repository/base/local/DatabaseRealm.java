package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.RealmHelper;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.NumberInputConfirm;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.Result;
import com.demo.architect.data.model.TimesConfirm;
import com.demo.architect.data.model.offline.DeliveryNoteModel;
import com.demo.architect.data.model.offline.DeliveryNoteWindowModel;
import com.demo.architect.data.model.offline.DetailError;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanConfirmModel;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.architect.data.model.offline.LogScanPackWindowModel;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.architect.data.model.offline.ProductPackWindowModel;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.model.offline.ProductWarehouseModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.architect.data.model.offline.WarehousingModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
                        .schemaVersion(7)
                        .migration(new MyMigration())
                        .build();
                Realm.setDefaultConfiguration(realmConfigurationMain);
                RealmHelper.getInstance().initRealm(true);
            }
            if (SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "").equals(Constants.SERVER_TEST)) {
                RealmConfiguration realmConfigurationTest = new RealmConfiguration.Builder()
                        .name(Constants.DATABASE_TEST)
                        .schemaVersion(7)
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
                    for (NumberInputConfirm numberInput : orderConfirmEntity.getListInputConfirmed()) {
                        if (numberInput.getTimesInput() == times) {
                            LogScanConfirmModel.createOrUpdate(realm, orderConfirmEntity, times, numberInput.getNumberConfirmed(), userId);
                            break;
                        }
                    }
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

    public RealmResults<LogListSerialPackPagkaging> getListScanPackaging() {
        Realm realm = getRealmInstance();
        RealmResults<LogListSerialPackPagkaging> listScanPackaging = LogScanPackaging.getListScanPackaging(realm);
        return listScanPackaging;
    }


    public void deleteScanPackaging(final long productId, final String sttPack, final String codePack, final long logId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.deleteLogScanPackaging(realm, productId, sttPack, codePack, logId);
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

    public ProductPackagingModel findProductPackaging(final long productId, long productDetailId, final String serialPack) {
        Realm realm = getRealmInstance();
        ProductPackagingModel productPackagingModel = ProductPackagingModel.findProductPackaging(realm, productId, productDetailId, serialPack);
        return productPackagingModel;
    }

    public int getTotalScanBySerialPack(long productId, String serialPack) {
        Realm realm = getRealmInstance();
        int total = LogScanPackaging.getTotalScan(realm, productId, serialPack);
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

    public int addBarcodeScanPackaging(final ListModuleEntity module, final PackageEntity packageEntity, final ProductPackagingEntity productPackagingEntity, final long orderId, final long apartmentId) {

        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.createOrUpdateLogScanPackaging(realm, module, packageEntity, productPackagingEntity, orderId, apartmentId, userId);
            }
        });

        final int total = LogScanPackaging.getTotalScan(realm, module.getProductId(), packageEntity.getSerialPack());
        return total;
    }

    public List<LogScanPackaging> getListScanPackaging(long orderId, long apartmentId, long moduleId, String serialPackId) {
        Realm realm = getRealmInstance();
        List<LogScanPackaging> result = LogScanPackaging.getListScanPackaging(realm,
                orderId, apartmentId, moduleId, serialPackId);
        return result;
    }

    public void updateStatusScanPackaging(final long logSerialId, final long serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackaging.updateStatusScanPackaging(realm, logSerialId, serverId);

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

    public void deleteQC(final long id, final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type == 4) {
                    QualityControlWindowModel.deleteQC(realm, id);
                } else {
                    QualityControlModel.deleteQC(realm, id);
                }
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
                RealmResults<LogListSerialPackPagkaging> logListSerialPackPagkagings = realm.where(LogListSerialPackPagkaging.class).findAll();
                logListSerialPackPagkagings.deleteAllFromRealm();

                RealmResults<LogScanConfirmModel> logScanConfirmModels = realm.where(LogScanConfirmModel.class).findAll();
                logScanConfirmModels.deleteAllFromRealm();
                RealmResults<LogScanPackaging> logScanPackagings = realm.where(LogScanPackaging.class).findAll();
                logScanPackagings.deleteAllFromRealm();
                RealmResults<LogScanStages> logScanStages = realm.where(LogScanStages.class).findAll();
                logScanStages.deleteAllFromRealm();
                RealmResults<ProductDetail> productDetails = realm.where(ProductDetail.class).findAll();
                productDetails.deleteAllFromRealm();
                RealmResults<ProductPackagingModel> productPackagingModels = realm.where(ProductPackagingModel.class).findAll();
                productPackagingModels.deleteAllFromRealm();
                RealmResults<QualityControlModel> qualityControlModels = realm.where(QualityControlModel.class).findAll();
                qualityControlModels.deleteAllFromRealm();

            }
        });
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
                RealmResults<ProductDetail> results = realm.where(ProductDetail.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();
            }
        });
    }

    public RealmResults<LogScanStages> getAllListStages() {
        Realm realm = getRealmInstance();
        RealmResults<LogScanStages> results = realm.where(LogScanStages.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;
    }

    public void deleteAllScanStages() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ProductDetail> results = realm.where(ProductDetail.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();

                RealmResults<LogScanStages> results1 = realm.where(LogScanStages.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
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
                        .equalTo("status", Constants.WAITING_UPLOAD).findAll();
                results.deleteAllFromRealm();

                RealmResults<LogScanStagesWindowModel> results1 = realm.where(LogScanStagesWindowModel.class)
                        .equalTo("status", Constants.WAITING_UPLOAD)
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
                .notEqualTo("statusConfirm", -1)
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

    public void deleteAlLQC(final int type) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (type == 4) {
                    QualityControlWindowModel.deleteAlLQC(realm);
                } else {

                    QualityControlModel.deleteAlLQC(realm);
                }
            }
        });
    }

    public void saveBarcodeQCWindow(final int machineId, final String violator, final int qcId, final ProductWindowEntity productEntity) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QualityControlWindowModel.create(realm, machineId, violator, qcId, productEntity, userId);
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

    public Result findProductPackagingByList(List<Result> list) {
        Realm realm = getRealmInstance();
        Result productPackagingModel = ProductPackagingModel.findProductPackagingByList(realm, list);
        return productPackagingModel;
    }

    public LogListSerialPackPagkaging getListDetailPackageById(long logSerialId) {
        Realm realm = getRealmInstance();
        LogListSerialPackPagkaging logListSerialPackPagkaging = LogListSerialPackPagkaging.getListDetailPackageById(realm, logSerialId);
        return logListSerialPackPagkaging;
    }

    public String getListDetailUploadPackageById(long logSerialId) {
        Realm realm = getRealmInstance();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        LogListSerialPackPagkaging logListSerialPackPagkaging = LogListSerialPackPagkaging.getListDetailPackageById(realm, logSerialId);
        RealmList<LogScanPackaging> list = logListSerialPackPagkaging.getList();
        List<LogScanPackaging> logScanPackagings = realm.copyFromRealm(list);

        return gson.toJson(logScanPackagings);
    }

    public ProductPackWindowModel getProductPackingWindow(ProductPackagingWindowEntity entity) {

        Realm realm = getRealmInstance();
        ProductPackWindowModel productDetail = ProductPackWindowModel.findProductPackaging(realm, entity.getProductSetDetailId(), entity.getProductSetId());
        if (productDetail == null) {
            productDetail = ProductPackWindowModel.create(realm, entity);
        }
        return productDetail;
    }

    public boolean saveBarcodeScanPackagingWindow(final long productId, final int direction, final GroupSetEntity groupSetEntity) {
        Realm realm = getRealmInstance();

        //kiểm tra có thỏa điều để lưu hay ko?
        boolean satisfy = LogScanPackWindowModel.checkCondition(realm, productId, groupSetEntity);
        if (satisfy) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LogScanPackWindowModel.createOrUpdateLogScanPackaging(realm, productId, direction, groupSetEntity, userId);
                }
            });
        }
        return satisfy;
    }

    public int getTotalNumberDetaiLInPackWindow(String packCode, int numberPack) {
        Realm realm = getRealmInstance();
        int total = LogScanPackWindowModel.getTotalScan(realm, packCode, numberPack);
        return total;
    }

    public void updateNumberScanPackagingWindow(final String packCode, final int numberOnPack, final long logId, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackWindowModel.updateNumberScanPackaging(realm, packCode, numberOnPack, logId, number);
            }
        });
    }

    public void deleteScanPackagingWindow(final long logId, final String packCode, final int numberOnPack) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackWindowModel.deleteLogScanPackaging(realm, logId, packCode, numberOnPack);
            }
        });
    }

    public void deleteAllItemLogScanPackagingWindow() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackWindowModel.deleteAllItemLogScanPackaging(realm);
            }
        });
    }

    public RealmResults<ListPackCodeWindowModel> getListScanPackagingWindow() {
        Realm realm = getRealmInstance();
        RealmResults<ListPackCodeWindowModel> results = LogScanPackWindowModel.getListScanPackaging(realm);
        return results;
    }

    public ListPackCodeWindowModel getListDetailPackWindowById(long mainId) {
        Realm realm = getRealmInstance();
        ListPackCodeWindowModel results = ListPackCodeWindowModel.getListDetailPackageById(realm, mainId);
        return results;
    }

    public String getListDetailUploadPackWindowById(long mainId) {
        Realm realm = getRealmInstance();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        ListPackCodeWindowModel main = ListPackCodeWindowModel.getListDetailPackageById(realm, mainId);
        RealmList<LogScanPackWindowModel> list = main.getList();
        List<LogScanPackWindowModel> result = realm.copyFromRealm(list);

        return gson.toJson(result);
    }

    public void updateStatusScanPackagingWindow(final long mainId, final long serverId) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LogScanPackWindowModel.updateStatusScanPackaging(realm, mainId, serverId);
            }
        });
    }

    public int getNumberScanWindowByBarcode(String packCode, int numberSetOnPack, String barcode) {
        Realm realm = getRealmInstance();
        int numberScan = LogScanPackWindowModel.getNumberScanWindowByBarcode(realm, packCode, numberSetOnPack, barcode);
        return numberScan;

    }

    public List<GroupScan> getListGroupScanVersion() {
        Realm realm = getRealmInstance();
        List<GroupScan> list = GroupScan.getListGroupScanVersion(realm);
        return list;

    }

    public ProductWarehouseModel getProductWarehouse(ProductWarehouseEntity entity) {
        Realm realm = getRealmInstance();
        ProductWarehouseModel productDetail = ProductWarehouseModel.getProductDetail(realm, entity, userId);
        if (productDetail == null) {
            productDetail = ProductWarehouseModel.create(realm, entity, userId);
        }
        return productDetail;
    }

    public void warehousing(final WarehousingModel model) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                WarehousingModel.add(realm, model);
            }
        });
    }

    public void deleteWarehousing(final long id) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                WarehousingModel.delete(realm, id);
            }
        });
    }

    public void updateNumberWarehousing(final long id, final int number) {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                WarehousingModel.updateNumberInput(realm, id, number);
            }
        });
    }

    public void deleteAllWarehousing() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<WarehousingModel> warehousingModels = realm.where(WarehousingModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
                warehousingModels.deleteAllFromRealm();

                RealmResults<ProductWarehouseModel> productWarehouseModels = realm.where(ProductWarehouseModel.class).equalTo("status", Constants.WAITING_UPLOAD).findAll();
                productWarehouseModels.deleteAllFromRealm();

            }
        });
    }

    public RealmResults<WarehousingModel> getAllListWarehousing() {
        Realm realm = getRealmInstance();
        RealmResults<WarehousingModel> results = WarehousingModel.getAllList(realm);
        return results;
    }

    public List<WarehousingModel> getListWarehousingWindowUpload() {
        Realm realm = getRealmInstance();
        RealmResults<WarehousingModel> results = WarehousingModel.getAllList(realm);
        return realm.copyFromRealm(results);
    }

    public void updateStatusWarehousing() {
        Realm realm = getRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                WarehousingModel.updateStatus(realm);

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


            if (oldVersion == 3) {
                schema.create("DeliveryNoteWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("outputId", long.class)
                        .addField("productSetId", long.class)
                        .addField("productSetName", String.class)
                        .addField("productSetDetailId", long.class)
                        .addField("productSetDetailName", String.class)
                        .addField("numberOut", int.class)
                        .addField("numberRest", int.class)
                        .addField("numberUsed", int.class)
                        .addField("numberConfirm", int.class)
                        .addField("status", int.class);

                schema.create("LogScanConfirmWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("outputId", long.class)
                        .addField("orderId", long.class)
                        .addField("departmentIDIn", int.class)
                        .addField("departmentIDOut", int.class)
                        .addField("productSetDetailId", long.class)
                        .addField("barcode", String.class)
                        .addField("numberTotalOrder", int.class)
                        .addField("numberOut", int.class)
                        .addField("numberConfirmed", int.class)
                        .addField("userId", long.class)
                        .addField("isPrint", boolean.class)
                        .addField("status", int.class)
                        .addField("statusConfirm", int.class)
                        .addField("dateConfirm", String.class)
                        .addField("state", boolean.class)
                        .addRealmObjectField("deliveryNoteModel", schema.get("DeliveryNoteWindowModel"));

                schema.remove("LogScanConfirm");

                schema.create("LogScanConfirmModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("outputId", long.class)
                        .addField("orderId", long.class)
                        .addField("departmentIDIn", int.class)
                        .addField("departmentIDOut", int.class)
                        .addField("productDetailId", long.class)
                        .addField("barcode", String.class)
                        .addField("numberTotalOrder", int.class)
                        .addField("numberOut", int.class)
                        .addField("numberConfirmed", int.class)
                        .addField("numberRestInTimes", int.class)
                        .addField("numberUsedInTimes", int.class)
                        .addField("userId", long.class)
                        .addField("timesInput", int.class)
                        .addField("status", int.class)
                        .addField("statusConfirm", int.class)
                        .addField("dateConfirm", String.class)
                        .addField("state", boolean.class)
                        .addRealmObjectField("deliveryNoteModel", schema.get("DeliveryNoteModel"));

                schema.create("ProductDetailWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("productSetDetailID", long.class)
                        .addField("productSetDetailName", String.class)
                        .addField("productSetDetailCode", String.class)
                        .addField("barcode", String.class)
                        .addField("productSetName", String.class)
                        .addField("numberTotal", int.class)
                        .addField("numberSuccess", int.class)
                        .addField("numberScanned", int.class)
                        .addField("numberRest", int.class)
                        .addField("userId", long.class)
                        .addField("status", int.class)
                        .addRealmListField("listStages", Integer.class);


                schema.create("LogScanStagesWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("departmentIdIn", int.class)
                        .addField("departmentIdOut", int.class)
                        .addField("staffId", int.class)
                        .addField("productSetDetailID", long.class)
                        .addField("barcode", String.class)
                        .addField("numberInput", int.class)
                        .addField("dateScan", String.class)
                        .addField("status", int.class)
                        .addField("userId", long.class)
                        .addRealmObjectField("productDetail", schema.get("ProductDetailWindowModel"));

                schema.create("QualityControlWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("departmentId", int.class)
                        .addField("machineId", int.class)
                        .addField("violator", String.class)
                        .addField("qcId", int.class)
                        .addField("productSetDetailId", long.class)
                        .addField("productSetDetailName", String.class)
                        .addField("productSetName", String.class)
                        .addField("barcode", String.class)
                        .addField("totalNumber", int.class)
                        .addField("listReason", String.class)
                        .addField("description", String.class)
                        .addRealmListField("idReasonList", Integer.class)
                        .addRealmListField("imageList", schema.get("ImageModel"))
                        .addField("listImage", String.class)
                        .addField("numberRest", int.class)
                        .addField("number", int.class)
                        .addField("status", int.class)
                        .addField("userId", long.class)
                        .addField("edit", boolean.class);
                schema.get("DeliveryNoteModel")
                        .addField("status", int.class)
                        .removeField("numberOut")
                        .addField("numberOut", int.class)
                        .removeField("numberRest")
                        .addField("numberRest", int.class)
                        .removeField("numberUsed")
                        .addField("numberUsed", int.class)
                        .removeField("numberConfirm")
                        .addField("numberConfirm", int.class)
                        .addField("productId", long.class)
                        .addField("module", String.class)
                        .addField("productDetailName", String.class)
                        .removeField("deliveryNoteId");
                schema.get("GroupCode")
                        .removeField("numberTotal")
                        .addField("numberTotal", int.class)
                        .removeField("number")
                        .addField("number", int.class);
                schema.get("LogScanStages")
                        .addField("status", int.class)
                        .removeField("numberInput")
                        .addField("numberInput", int.class)
                        .removeField("numberGroup")
                        .addField("numberGroup", int.class);
                schema.get("ProductDetail")
                        .addField("status", int.class)
                        .addField("numberRest", int.class)
                        .addField("numberScanned", int.class)
                        .addField("orderId", long.class)
                        .addField("productDetailId", long.class)
                        .addField("barcode", String.class)
                        .addField("module", String.class)
                        .addField("times", int.class)
                        .addField("numberTotal", int.class)
                        .addField("numberSuccess", int.class)
                        .removeField("productId")
                        .removeField("listInput");

                schema.get("QualityControlModel")
                        .addField("qcCode", String.class)
                        .addField("violator", String.class)
                        .addField("machineName", String.class);
                oldVersion++;
            }

            if (oldVersion == 4) {
                schema.get("LogListSerialPackPagkaging")
                        .removeField("numberTotal")
                        .addField("numberTotal", int.class)
                        .addField("orderId", long.class)
                        .addField("apartmentId", long.class)
                        .addField("status", int.class)
                        .addField("serverId", long.class);
                schema.get("ProductPackagingModel")
                        .removeField("numberRest")
                        .addField("numberRest", int.class)
                        .removeField("numberScan")
                        .addField("numberScan", int.class)
                        .removeField("numberTotal")
                        .addField("numberTotal", int.class)
                        .addField("productId", long.class);
                schema.get("LogScanPackaging")
                        .removeField("numberInput")
                        .addField("numberInput", int.class)
                        .removeField("serverId")
                        .removeField("orderId")
                        .removeField("module");

                oldVersion++;
            }
            if (oldVersion == 5) {
                schema.create("ProductPackWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("productSetId", long.class)
                        .addField("productSetDetailId", long.class)
                        .addField("productSetDetailName", String.class)
                        .addField("productSetDetailCode", String.class)
                        .addField("barcode", String.class)
                        .addField("codeOrigin", String.class)
                        .addField("width", double.class)
                        .addField("length", double.class)
                        .addField("height", double.class)
                        .addField("numberTotal", int.class)
                        .addField("numberScan", int.class)
                        .addField("numberScaned", int.class)
                        .addField("numberRest", int.class);

                schema.create("LogScanPackWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("productSetDetailId", long.class)
                        .addField("numberOnSet", int.class)
                        .addField("barcode", String.class)
                        .addField("numberScan", int.class)
                        .addField("dateScan", String.class)
                        .addField("statusScan", int.class)
                        .addField("status", int.class)
                        .addField("userId", int.class)
                        .addRealmObjectField("productPackWindowModel", schema.get("ProductPackWindowModel"));

                schema.create("ListPackCodeWindowModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("productSetId", long.class)
                        .addField("direction", int.class)
                        .addField("numberSetOnPack", int.class)
                        .addField("packCode", String.class)
                        .addField("totalNumber", int.class)
                        .addField("serverId", long.class)
                        .addField("status", int.class)
                        .addField("userId", int.class)
                        .addRealmListField("list", schema.get("LogScanPackWindowModel"));


                oldVersion++;
            }
            if (oldVersion == 6) {
                schema.create("ProductWarehouseModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("productId", long.class)
                        .addField("productName", String.class)
                        .addField("productCode", String.class)
                        .addField("pack", String.class)
                        .addField("numberTotal", int.class)
                        .addField("numberSuccess", int.class)
                        .addField("numberScanned", int.class)
                        .addField("numberRest", int.class)
                        .addField("userId", long.class)
                        .addField("status", int.class);
                schema.create("WarehousingModel")
                        .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                        .addField("orderId", long.class)
                        .addField("productId", long.class)
                        .addField("barcode", String.class)
                        .addField("pack", String.class)
                        .addField("number", int.class)
                        .addField("numberInput", int.class)
                        .addField("numberPack", int.class)
                        .addField("longitude", double.class)
                        .addField("latitude", double.class)
                        .addField("dateScan", String.class)
                        .addField("status", int.class)
                        .addField("userId", long.class)
                        .addRealmObjectField("productModel", schema.get("ProductWarehouseModel"));
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
