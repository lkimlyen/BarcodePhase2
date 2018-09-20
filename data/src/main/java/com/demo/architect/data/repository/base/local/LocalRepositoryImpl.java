package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.model.offline.QualityControlModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class LocalRepositoryImpl implements LocalRepository {

    DatabaseRealm databaseRealm;

    public LocalRepositoryImpl(Context context) {
        databaseRealm = new DatabaseRealm(context);
    }

    @Override
    public Observable<String> add(final MessageModel model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);

                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<MessageModel>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<MessageModel>>() {
            @Override
            public void call(Subscriber<? super List<MessageModel>> subscriber) {
                try {
                    List<MessageModel> models = databaseRealm.findAll(MessageModel.class);

                    subscriber.onNext(models);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countLogScanStages(final int orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countLogScanStagesWatingUpload(orderId, departmentId, times);

                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countAllDetailWaitingUpload(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countAllDetailWaitingUpload(orderId);

                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanStages>> getListLogScanStagesUpdate(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super List<LogScanStages>> subscriber) {
                try {
                    List<LogScanStages> list = databaseRealm.getListLogScanStagesUpload(orderId);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable <HashMap<List<LogScanStages>, Set<GroupScan>>> getListLogScanStagesUpdate() {
        return Observable.create(new Observable.OnSubscribe< HashMap<List<LogScanStages>, Set<GroupScan>>>() {
            @Override
            public void call(Subscriber<? super HashMap<List<LogScanStages>, Set<GroupScan>>> subscriber) {
                try {
                    HashMap<List<LogScanStages>, Set<GroupScan>> list = databaseRealm.getListLogScanStagesUpload();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanStages>> getListScanStages(final int orderId, final int departmentIdOut, final int times, final String module) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanStages>> subscriber) {
                try {
                    RealmResults<LogScanStages> list = databaseRealm.getListScanStagesByModule(orderId,
                            departmentIdOut, times, module);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<GroupCode>> getListGroupCodeScan(final int orderId, final String module) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<GroupCode>>() {
            @Override
            public void call(Subscriber<? super RealmResults<GroupCode>> subscriber) {
                try {
                    RealmResults<GroupCode> list = databaseRealm.getListGroupCodeByModule(orderId, module);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> addLogScanStagesAsync(final LogScanStages model, final ProductEntity productEntity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addLogScanStagesAsync(model, productEntity);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductDetail> getProductDetail(final ProductEntity product) {
        return Observable.create(new Observable.OnSubscribe<ProductDetail>() {
            @Override
            public void call(Subscriber<? super ProductDetail> subscriber) {
                try {
                    ProductDetail productDetail = databaseRealm.getProductDetail(product);
                    subscriber.onNext(productDetail);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanStages(final int stagesId, final int numberInput) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberScanStages(stagesId, numberInput);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanStages(final int stagesId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanStages(stagesId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogListScanStages> getListScanStagseByDepartment(final int orderId, final int departmentId,
                                                                       final int userId, final int times) {
        return Observable.create(new Observable.OnSubscribe<LogListScanStages>() {
            @Override
            public void call(Subscriber<? super LogListScanStages> subscriber) {
                try {
                    LogListScanStages logListScanStages = databaseRealm.getListScanStages(orderId, departmentId, userId, times);
                    subscriber.onNext(logListScanStages);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addOrderConfirm(list);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanConfirm>> getListConfirm(final int orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanConfirm>> subscriber) {
                try {
                    RealmResults<LogScanConfirm> realmList = databaseRealm.getListConfirm(orderId, departmentIdOut, times);
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countListConfirmByTimesWaitingUpload(final int orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int count = databaseRealm.countListConfirmByTimesWaitingUpload(orderId, departmentIdOut, times);
                    subscriber.onNext(count);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanConfirm>> getListLogScanConfirm() {
        return Observable.create(new Observable.OnSubscribe<List<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super List<LogScanConfirm>> subscriber) {
                try {
                    List<LogScanConfirm> realmList = databaseRealm.getListLogScanConfirm();
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanConfirm> findConfirmByBarcode(final int orderId, final int departmentIdOut, final int times, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<LogScanConfirm>() {
            @Override
            public void call(Subscriber<? super LogScanConfirm> subscriber) {
                try {
                    LogScanConfirm model = databaseRealm.findConfirmByBarcode(barcode, orderId, departmentIdOut,
                            times);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> updateNumnberLogConfirm(final int orderId, final int orderProductId, final int departmentIdOut, final int times, final int numberScan, final boolean scan) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberLogConfirm(orderId, orderProductId, departmentIdOut, times, numberScan, scan);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusLogConfirm() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusConfirm();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanStagesByOrder(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanStagesByOrder(orderId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanStages() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanStages();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusAndServerIdImage(final int id, final int imageId, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusAndServerIdImage(id, imageId, serverId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addImageModel(final int id, final String pathFile) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addImageModel(id, pathFile);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteImageModel(final int id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteImageModel(id);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogListModulePagkaging>> getListScanPackaging(final SOEntity soEntity, final ApartmentEntity apartment) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogListModulePagkaging>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogListModulePagkaging>> subscriber) {
                try {
                    RealmResults<LogListModulePagkaging> listScanPackaging = databaseRealm.getListScanPackaging(soEntity, apartment);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanPackaging>> getListScanPackaging(final int orderId, final int apartmentId, final int moduleId, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanPackaging>>() {
            @Override
            public void call(Subscriber<? super List<LogScanPackaging>> subscriber) {
                try {
                    List<LogScanPackaging> listScanPackaging = databaseRealm.getListScanPackaging(orderId,
                            apartmentId, moduleId, serialPack);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> saveBarcodeScanPackaging(final ListModuleEntity module, final PackageEntity packageEntity, final ProductPackagingEntity productPackagingEntity, final int orderId, final int apartmentId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addBarcodeScanPackaging(module, packageEntity, productPackagingEntity, orderId, apartmentId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanPackaging(final int logId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanPackaging(logId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanPackaging(final int logId, final int number) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberScanPackaging(logId, number);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<ProductPackagingModel> findProductPackaging(final int productId, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<ProductPackagingModel>() {
            @Override
            public void call(Subscriber<? super ProductPackagingModel> subscriber) {
                try {
                    ProductPackagingModel model = databaseRealm.findProductPackaging(productId, serialPack);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogListOrderPackaging> findOrderPackaging(final int orderId) {
        return Observable.create(new Observable.OnSubscribe<LogListOrderPackaging>() {
            @Override
            public void call(Subscriber<? super LogListOrderPackaging> subscriber) {
                try {
                    LogListOrderPackaging model = databaseRealm.findOrderPackaging(orderId);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> getTotalScanBySerialPack(final int orderId, final int apartmentId, final int moduleId, final String serialPack) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int total = databaseRealm.getTotalScanBySerialPack(orderId, apartmentId, moduleId, serialPack);
                    subscriber.onNext(total);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGroupCode(final String groupCode, final int orderId, final String module, final GroupCode[] listSelect) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGroupCode(groupCode, orderId, module, listSelect);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGroupCode(final String groupCode, final LogScanStages logScanStages, final ProductEntity productEntity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGroupCode(groupCode, logScanStages, productEntity);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> updateNumberGroup(final int group, final int numberGroup) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    databaseRealm.updateNumberGroup(group, numberGroup);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> detachedCodeStages(final List<ProductGroupEntity> list, final int orderId, final String module, final String groupCode) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.detachedCodeStages(list, orderId, module, groupCode);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> removeItemInGroup(final ProductGroupEntity logScanStages, final int orderId, final String module) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.removeItemInGroup(logScanStages, orderId, module);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<IPAddress> insertOrUpdateIpAddress(final IPAddress model) {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    databaseRealm.insertOrUpdate(model);
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusScanPackaging(final int orderId, final int apartmentId, final int moduleId, final String serialPack, final int serverId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanPackaging(orderId, apartmentId, moduleId, serialPack, serverId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteAllItemLogScanPackaging() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteAllItemLogScanPackaging();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<QualityControlModel>> getListQualityControl(final int orderId, final int departmentId) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<QualityControlModel>>() {
            @Override
            public void call(Subscriber<? super RealmResults<QualityControlModel>> subscriber) {
                try {
                    RealmResults<QualityControlModel> results = databaseRealm.getListQualityControl(orderId, departmentId);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<QualityControlModel> getDetailQualityControl(final int id) {
        return Observable.create(new Observable.OnSubscribe<QualityControlModel>() {
            @Override
            public void call(Subscriber<? super QualityControlModel> subscriber) {
                try {
                    QualityControlModel results = databaseRealm.getDetailQualityControl(id);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmList<Integer>> getListReasonQualityControl(final int id) {
        return Observable.create(new Observable.OnSubscribe<RealmList<Integer>>() {
            @Override
            public void call(Subscriber<? super RealmList<Integer>> subscriber) {
                try {
                    RealmList<Integer> results = databaseRealm.getListReasonQualityControl(id);
                    subscriber.onNext(results);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveBarcodeQC(final int orderId, final int departmentId, final ProductEntity productEntity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveBarcodeQC(orderId, departmentId, productEntity);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateDetailErrorQC(final int id, final int numberFailed, final String description, final Collection<Integer> idList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateDetailErrorQC(id, numberFailed, description, idList);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<QualityControlModel>> getListQualityControlUpload() {
        return Observable.create(new Observable.OnSubscribe<List<QualityControlModel>>() {
            @Override
            public void call(Subscriber<? super List<QualityControlModel>> subscriber) {
                try {
                    List<QualityControlModel> result = databaseRealm.getListQualityControlUpload();
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateImageIdAndStatus(final int qcId, final int id, final int imageId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateImageIdAndStatus(qcId, id, imageId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusQC() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusQC();
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGroupCode(final ProductEntity productEntity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGroupCode(productEntity);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> addGroupScan(final List<GroupEntity> list) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGroupScan(list);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> checkNumberProductInGroupCode(final ProductEntity model) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    boolean b = databaseRealm.checkNumberProductInGroupCode(model);
                    subscriber.onNext(b);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> updateGroupCode(final String groupCode, final int orderId, final String module, final GroupCode[] listSelect) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateGroupCode(groupCode, orderId, module, listSelect);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> confirmAllProductReceive(final int orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.confirmAllProductReceive(orderId, departmentId, times);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> cancelConfirmAllProductReceive(final int orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.cancelConfirmAllProductReceive(orderId, departmentId, times);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<IPAddress> findIPAddress() {
        return Observable.create(new Observable.OnSubscribe<IPAddress>() {
            @Override
            public void call(Subscriber<? super IPAddress> subscriber) {
                try {
                    IPAddress ipAddress = databaseRealm.findFirst(IPAddress.class);
                    subscriber.onNext(ipAddress);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> getCheckedConfirmAll(final int orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Boolean aBoolean = databaseRealm.getCheckedConfirmAll(orderId, departmentIdOut, times);
                    subscriber.onNext(aBoolean);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
