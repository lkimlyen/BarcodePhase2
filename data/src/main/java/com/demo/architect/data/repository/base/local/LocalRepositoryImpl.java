package com.demo.architect.data.repository.base.local;

import android.content.Context;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.MessageModel;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
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
    public Observable<Integer> countLogScanStages(final long orderId, final int departmentId, final int times) {
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
    public Observable<Integer> countAllDetailWaitingUpload(final long orderId) {
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
    public Observable<List<LogScanStages>> getListLogScanStagesUpdate(final long orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super List<LogScanStages>> subscriber) {
                try {
                    List<LogScanStages> list = databaseRealm.getListLogScanStagesUpload(orderId,departmentId,times);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<HashMap<List<LogScanStages>, Set<GroupScan>>> getListLogScanStagesUpdate() {
        return Observable.create(new Observable.OnSubscribe<HashMap<List<LogScanStages>, Set<GroupScan>>>() {
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
    public Observable<RealmResults<LogScanStages>> getListScanStages(final long orderId, final int departmentIdOut, final int times, final String module) {
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
    public Observable<RealmResults<GroupCode>> getListGroupCodeScan(final long orderId) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<GroupCode>>() {
            @Override
            public void call(Subscriber<? super RealmResults<GroupCode>> subscriber) {
                try {
                    RealmResults<GroupCode> list = databaseRealm.getListGroupCode(orderId);
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
    public Observable<ProductDetail> getProductDetail(final ProductEntity product, final int times) {
        return Observable.create(new Observable.OnSubscribe<ProductDetail>() {
            @Override
            public void call(Subscriber<? super ProductDetail> subscriber) {
                try {
                    ProductDetail productDetail = databaseRealm.getProductDetail(product,times);
                    subscriber.onNext(productDetail);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberScanStages(final long stagesId, final double numberInput) {
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
    public Observable<String> deleteScanStages(final long stagesId) {
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
    public Observable<LogListScanStages> getListScanStagseByDepartment(final long orderId, final int departmentId,
                                                                       final long userId, final int times) {
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
    public Observable<String> addOrderConfirm(final List<OrderConfirmEntity> list, final long deliveryNoteId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addOrderConfirm(list,deliveryNoteId);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<LogScanConfirm>> getListConfirm(final long  deliveryNoteId, final long orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanConfirm>> subscriber) {
                try {
                    RealmResults<LogScanConfirm> realmList = databaseRealm.getListConfirm(deliveryNoteId,orderId, departmentIdOut, times);
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> countListConfirmByTimesWaitingUpload(final long orderId, final int departmentIdOut, final int times) {
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
    public Observable<List<LogScanConfirm>> getListLogScanConfirm(final long orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanConfirm>>() {
            @Override
            public void call(Subscriber<? super List<LogScanConfirm>> subscriber) {
                try {
                    List<LogScanConfirm> realmList = databaseRealm.getListLogScanConfirm(orderId, departmentIdOut, times);
                    subscriber.onNext(realmList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<LogScanConfirm> findConfirmByBarcode(final long maPhieuId, final long orderId, final int departmentIdOut, final int times, final String barcode) {
        return Observable.create(new Observable.OnSubscribe<LogScanConfirm>() {
            @Override
            public void call(Subscriber<? super LogScanConfirm> subscriber) {
                try {
                    LogScanConfirm model = databaseRealm.findConfirmByBarcode(maPhieuId,barcode, orderId, departmentIdOut,
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
    public Observable<String> updateNumnberLogConfirm(final long maPhieuId, final long orderId, final long orderProductId, final int departmentIdOut, final int times, final double numberScan, final boolean scan) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberLogConfirm(maPhieuId,orderId, orderProductId, departmentIdOut, times, numberScan, scan);
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
    public Observable<String> updateStatusScanStagesByOrder(final long orderId) {
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
    public Observable<String> updateStatusScanStages(final long orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusScanStages(orderId,departmentId,times);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusAndServerIdImage(final long id, final long imageId, final long serverId) {
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
    public Observable<String> addImageModel(final long id, final String pathFile) {
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
    public Observable<String> deleteImageModel(final long id) {
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
    public Observable<RealmResults<LogListSerialPackPagkaging>> getListScanPackaging(final SOEntity soEntity, final ApartmentEntity apartment) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogListSerialPackPagkaging>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogListSerialPackPagkaging>> subscriber) {
                try {
                    RealmResults<LogListSerialPackPagkaging> listScanPackaging = databaseRealm.getListScanPackaging(soEntity, apartment);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<LogScanPackaging>> getListScanPackaging(final long orderId, final long apartmentId, final long moduleId, final String serialPackId) {
        return Observable.create(new Observable.OnSubscribe<List<LogScanPackaging>>() {
            @Override
            public void call(Subscriber<? super List<LogScanPackaging>> subscriber) {
                try {
                    List<LogScanPackaging> listScanPackaging = databaseRealm.getListScanPackaging(orderId,
                            apartmentId, moduleId, serialPackId);
                    subscriber.onNext(listScanPackaging);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> saveBarcodeScanPackaging(final ListModuleEntity module, final PackageEntity packageEntity, final ProductPackagingEntity productPackagingEntity, final long orderId, final long apartmentId) {
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
    public Observable<String> deleteScanPackaging(final long logId) {
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
    public Observable<String> updateNumberScanPackaging(final long logId, final double number) {
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
    public Observable<ProductPackagingModel> findProductPackaging(final long productId, final String serialPack) {
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
    public Observable<LogListOrderPackaging> findOrderPackaging(final long orderId) {
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
    public Observable<Integer> getTotalScanBySerialPack(final long orderId, final long apartmentId, final long moduleId, final String serialPack) {
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
    public Observable<String> addGroupCode(final String groupCode, final long orderId, final GroupCode[] listSelect) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.addGroupCode(groupCode, orderId, listSelect);
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
    public Observable<Boolean> updateNumberGroup(final long group, final double numberGroup) {
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
    public Observable<String> detachedCodeStages(final List<ProductGroupEntity> list, final long orderId, final String groupCode) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.detachedCodeStages(list, orderId, groupCode);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> removeItemInGroup(final ProductGroupEntity logScanStages, final long orderId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.removeItemInGroup(logScanStages, orderId);
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
    public Observable<String> updateStatusScanPackaging(final long orderId, final long apartmentId, final long moduleId, final String serialPack, final long serverId) {
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
    public Observable<String> deleteQC(final long id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteQC(id);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<RealmResults<QualityControlModel>> getListQualityControl(final long orderId, final int departmentId) {
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
    public Observable<QualityControlModel> getDetailQualityControl(final long id) {
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
    public Observable<RealmList<Integer>> getListReasonQualityControl(final long id) {
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
    public Observable<String> saveBarcodeQC(final long orderId, final int departmentId, final ProductEntity productEntity) {
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
    public Observable<String> updateDetailErrorQC(final long id, final double numberFailed, final String description, final Collection<Integer> idList) {
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
    public Observable<String> updateImageIdAndStatus(final long qcId, final long id, final long imageId) {
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
    public Observable<String> updateGroupCode(final String groupCode, final long orderId, final GroupCode[] listSelect) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateGroupCode(groupCode, orderId, listSelect);
                    subscriber.onNext("Success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    @Override
    public Observable<String> confirmAllProductReceive(final long orderId, final int departmentId, final int times) {
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
    public Observable<String> cancelConfirmAllProductReceive(final long orderId, final int departmentId, final int times) {
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
    public Observable<Boolean> getCheckedConfirmAll(final long orderId, final int departmentIdOut, final int times) {
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

    @Override
    public Observable<RealmResults<LogScanStages>> getScanByProductDetailId(final LogScanStages logScanStages) {
        return Observable.create(new Observable.OnSubscribe<RealmResults<LogScanStages>>() {
            @Override
            public void call(Subscriber<? super RealmResults<LogScanStages>> subscriber) {
                try {
                    RealmResults<LogScanStages> list = databaseRealm.getScanByProductDetailId(logScanStages);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteScanGroupCode(final long id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteScanGroupCode(id);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Double> totalNumberScanGroup(final long productDetailId) {
        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                try {
                    Double aDouble = databaseRealm.totalNumberScanGroup(productDetailId);
                    subscriber.onNext(aDouble);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateStatusPrint(final long orderId, final int departmentIdOut, final int times) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateStatusPrint(orderId,departmentIdOut,times);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> updateNumberTotalProduct(final List<ProductEntity> entity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.updateNumberTotalProduct(entity);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> deleteDataLocal() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.deleteDataLocal();
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<GroupScan>> getListGroupScanVersion(final long orderId, final int departmentId, final int times) {
        return Observable.create(new Observable.OnSubscribe<List<GroupScan>>() {
            @Override
            public void call(Subscriber<? super List<GroupScan>> subscriber) {
                try {
                    List<GroupScan> result = databaseRealm.getListGroupScanVersion(orderId,departmentId,times);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> saveListProductDetail(final List<ProductEntity> entity) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.saveListProductDetail(entity);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
